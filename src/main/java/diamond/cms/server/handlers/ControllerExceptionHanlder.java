package diamond.cms.server.handlers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.Error;

@ControllerAdvice
public class ControllerExceptionHanlder{

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result resolveException(Exception ex) {
		Result result = new Result();
		result.setSuccess(false);
		AppException appEx;
		if (ex instanceof MethodArgumentNotValidException){
			MethodArgumentNotValidException vaildEx = (MethodArgumentNotValidException) ex;
			String errorMsg = vaildEx.getBindingResult().getFieldError().getDefaultMessage();
			appEx = new AppException(Error.PARAMS_ERROR, errorMsg);
		} else {
			ex.printStackTrace();
			appEx = new AppException();
		}
		result.setCode(appEx.getCode());
		result.setMsg(appEx.getMessage());
		return result;
	}

	@ExceptionHandler(AppException.class)
	@ResponseBody
	public Result appException (AppException ex) {
	    Result result = new Result();
	    result.setSuccess(false);
	    result.setCode(ex.getCode());
	    result.setMsg(ex.getMessage());
        return result;
	}
}
