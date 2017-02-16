package diamond.cms.server.valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
