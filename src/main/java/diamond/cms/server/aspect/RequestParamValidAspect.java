package diamond.cms.server.aspect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import diamond.cms.server.valid.FieldError;
import diamond.cms.server.valid.ParamValidException;

@Component
@Aspect
public class RequestParamValidAspect implements HandlerInterceptor{

    Logger log = LoggerFactory.getLogger(getClass());

    HandlerMethod handlerMethod;

    @Pointcut("execution(* diamond.cms.server.controllers.*.*(..))")
    public void controllerBefore(){};

    @Before("controllerBefore()")
    public void before(JoinPoint point) throws NoSuchMethodException, SecurityException, ParamValidException{
        Object [] args = point.getArgs();
        Set<ConstraintViolation<Object>> validResult = ValidMethodParams(handlerMethod.getBean(), handlerMethod.getMethod(), args);
        if (!validResult.isEmpty()) {
            MethodParameter [] methodParams = handlerMethod.getMethodParameters();
            List<FieldError> errors = validResult.stream().map(v -> {
                PathImpl pathImpl = (PathImpl) v.getPropertyPath();
                int paramIndex = pathImpl.getLeafNode().getParameterIndex();
                String paramName = methodParams[paramIndex].getParameterName();
                FieldError error = new FieldError();
                error.setName(paramName);
                error.setMessage(v.getMessage());
                return error;
            }).collect(Collectors.toList());
            throw new ParamValidException(errors);
        }
    }

    public static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    public static final ExecutableValidator VALIDATOR = FACTORY.getValidator().forExecutables();

    public static final <T> Set<ConstraintViolation<T>> ValidMethodParams(T obj, Method method, Object [] params){
        return VALIDATOR.validateParameters(obj,method, params);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
