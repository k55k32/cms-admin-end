package diamond.cms.server.config;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import diamond.cms.server.mvc.interceptor.AuthorizationInterceptor;
import diamond.cms.server.mvc.interceptor.CORSInterceptor;
import diamond.cms.server.mvc.json.spring.JsonReturnHandler;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    AuthorizationInterceptor authorizationInterceptor;

    @Resource
    CORSInterceptor crosInterceptor;

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        return processor;
    }

    @Bean
    public JsonReturnHandler JsonReturnHandler(){
        return new JsonReturnHandler();
    }

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(1024 * 1024 * 20);
        return resolver;
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(JsonReturnHandler());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crosInterceptor);
        registry.addInterceptor(authorizationInterceptor);
    }

}