package diamond.cms.server.controllers.advice;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.exceptions.Error;

@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice{

    @ExceptionHandler(AppException.class)
    public Result appException(AppException ex) {
        return appExceptionToResult(ex);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Result authException(HttpServletResponse response) {
        response.setStatus(401);
        return appExceptionToResult(new AppException(Error.UN_AUTHORIZATION));
    }


    @ExceptionHandler(Exception.class)
    public Result exception(Exception ex) {
        ex.printStackTrace();
        return appExceptionToResult(new AppException(Error.UNKNOW_EXCEPTION));
    }

    private Result appExceptionToResult(AppException ex) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ex.getCode());
        result.setMsg(ex.getMessage());
        return result;
    }

}
