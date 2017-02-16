package diamond.cms.server.mvc.valid.aspect;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import diamond.cms.server.mvc.valid.FieldError;
import diamond.cms.server.mvc.valid.ParamValidException;

@Component
@Aspect
public class RequestParamValidAspect{

    Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* diamond.cms.server.mvc.controllers.*.*(..))")
    public void controllerBefore(){};

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Before("controllerBefore()")
    public void before(JoinPoint point) throws NoSuchMethodException, SecurityException, ParamValidException{
        Object target = point.getThis();
        Object [] args = point.getArgs();
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        if (!validResult.isEmpty()) {
            String [] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            List<FieldError> errors = validResult.stream().map(constraintViolation -> {
                PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
                int paramIndex = pathImpl.getLeafNode().getParameterIndex();
                String paramName = parameterNames[paramIndex];
                FieldError error = new FieldError();
                error.setName(paramName);
                error.setMessage(constraintViolation.getMessage());
                return error;
            }).collect(Collectors.toList());
            throw new ParamValidException(errors);
        }
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final ExecutableValidator validator = factory.getValidator().forExecutables();

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        return validator.validateParameters(obj, method, params);
    }

}
