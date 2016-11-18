package diamond.cms.server.handlers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.exceptions.Error;

@Component
public class CustomerExceptionHanlder implements HandlerExceptionResolver{

    public static final int APPEXCEPTION_CODE = 422;
    public static final int UN_AUTHORIZATION = 401;
    public static final int SERVICE_ERROR_CODE = 500;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        return new ModelAndView(new View() {
            @Override
            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
                AppException appEx;
                response.setStatus(APPEXCEPTION_CODE);
                if (ex instanceof AppException) {
                    appEx = (AppException) ex;
                } else if (ex instanceof MethodArgumentNotValidException){
                    MethodArgumentNotValidException vaildEx = (MethodArgumentNotValidException) ex;
                    String errorMsg = vaildEx.getBindingResult().getFieldError().getDefaultMessage();
                    appEx = new AppException(Error.PARAMS_ERROR, errorMsg);
                } else if (ex instanceof AuthorizationException ){
                    appEx = new AppException(Error.UN_AUTHORIZATION);
                    response.setStatus(UN_AUTHORIZATION);
                }else {
                    ex.printStackTrace();
                    appEx = new AppException();
                    response.setStatus(SERVICE_ERROR_CODE);
                }
                String allow = Optional.ofNullable(response.getHeader("Access-Control-Allow-Origin")).orElse("*");
                response.setHeader("Access-Control-Allow-Origin", allow);

                WriterErrorToResponse(appEx, response);
            }

            @Override
            public String getContentType() {
                return MediaType.APPLICATION_JSON_UTF8_VALUE;
            }
        });
    }

    public static final void WriterErrorToResponse(AppException appEx, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(appEx.getCode());
        result.setMsg(appEx.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), result);
    }
}
