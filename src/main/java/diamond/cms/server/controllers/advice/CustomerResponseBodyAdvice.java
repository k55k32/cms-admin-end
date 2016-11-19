package diamond.cms.server.controllers.advice;

import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import diamond.cms.server.core.Result;

/**
 * @author Diamond
 *
 */
@ControllerAdvice
public class CustomerResponseBodyAdvice implements ResponseBodyAdvice<Object>{

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
	    Type type = returnType.getGenericParameterType();
		boolean flag = type.equals(Result.class) || type.equals(String.class);
		return !flag;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
	    if (body instanceof Result || body instanceof String) { // 不处理Result 和 String
	        return body;
	    }
		Result result = new Result();
		result.setSuccess(true);
		result.setData(body);
		return result;
	}

}
