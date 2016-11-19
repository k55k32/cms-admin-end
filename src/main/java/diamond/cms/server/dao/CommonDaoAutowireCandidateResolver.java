package diamond.cms.server.dao;

import org.jooq.Configuration;
import org.jooq.Schema;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class CommonDaoAutowireCandidateResolver extends QualifierAnnotationAutowireCandidateResolver implements BeanFactoryPostProcessor {

    private AutowireCandidateResolver parent = null;

    private Schema schema = null;
    private Configuration configuration = null;
    private BeanFactory beanFactory = null;

    @SuppressWarnings({ "rawtypes" })
    @Override
    public Object getSuggestedValue(DependencyDescriptor descriptor) {
        Object value = parent.getSuggestedValue(descriptor);
        if (value != null) {
            return value;
        }

        value = super.getSuggestedValue(descriptor);
        if (value != null) {
            return value;
        }


        Class<?> clazz = descriptor.getDependencyType();
        if (!clazz.equals(CommonDao.class)) {
            return null;
        }

        Class<?> entityClass = descriptor.getResolvableType().getGenerics()[0].getRawClass();
        if (entityClass == null) {
            ResolvableType type = descriptor.getResolvableType().getGeneric(0);
            entityClass = type.resolve();
        }
        if (configuration == null) {
            schema = beanFactory.getBean(Schema.class);
            configuration = beanFactory.getBean(Configuration.class);
        }

        CommonDao dao = new CommonDao<>(entityClass, schema, configuration);
        return dao;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        DefaultListableBeanFactory dlbf = (DefaultListableBeanFactory) factory;
        parent = dlbf.getAutowireCandidateResolver();
        dlbf.setAutowireCandidateResolver(this);

        beanFactory = factory;
    }


}