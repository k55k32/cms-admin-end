package diamond.cms.server.core.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;

public class ParamValidException extends Exception{

    private List<FieldError> fieldErrors = new ArrayList<>();

    /**
     *
     */
    private static final long serialVersionUID = -716441870779206738L;

    @Override
    public String getMessage() {
        return fieldErrors.toString();
    }

    public ParamValidException(ConstraintViolationException violationException, MethodParameter[] methodParameters) {
        List<FieldError> errors = violationException.getConstraintViolations().stream().map(constraintViolation -> {
            PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
            int paramIndex = pathImpl.getLeafNode().getParameterIndex();
            String paramName = methodParameters[paramIndex].getParameterName();
            FieldError error = new FieldError();
            error.setName(paramName);
            error.setMessage(constraintViolation.getMessage());
            return error;
        }).collect(Collectors.toList());
        this.fieldErrors = errors;
    }

    public ParamValidException(List<FieldError> errors) {
        this.fieldErrors = errors;
    }

    public ParamValidException(BindException ex) {
        this.fieldErrors = bindExceptionToFieldError(ex);
    }

    private List<FieldError> bindExceptionToFieldError(BindException ex) {
        return ex.getFieldErrors().stream().map(f -> {
           FieldError error = new FieldError();
           error.setName(f.getObjectName() + "." + f.getField());
           error.setMessage(f.getDefaultMessage());
           return error;
        }).collect(Collectors.toList());
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
