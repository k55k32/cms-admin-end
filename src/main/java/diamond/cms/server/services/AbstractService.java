package diamond.cms.server.services;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractService<T,P extends Serializable> {

    @Resource
    SessionFactory sessionFactory;

    Class<?> clazz;

    public AbstractService() {
        Type type = getClass().getGenericSuperclass();
        if(!(type instanceof ParameterizedType)){
            type = getClass().getSuperclass().getGenericSuperclass();
        }
        clazz = (Class<?>)((ParameterizedType)type).getActualTypeArguments()[0];
    }

    final Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public final T get(P id) {
        return (T) getSession().get(clazz, id);
    }

    public final void delete(T t) {
        getSession().delete(t);
    }

    public final void delete(P id) {
        getSession().delete(this.get(id));
    }

    public final T save (T t) {
        getSession().save(t);
        return t;
    }

    public final T update (T t) {
        getSession().update(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    public final List<T> list() {
        return createQuery("from " + clazz.getSimpleName()).list();
    }

    final Query createQuery(String hql){
        return getSession().createQuery(hql);
    }
}
