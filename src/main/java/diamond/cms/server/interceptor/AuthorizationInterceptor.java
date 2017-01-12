package diamond.cms.server.interceptor;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.controllers.ControllerUtils;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.model.User;
import diamond.cms.server.services.UserService;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor{

    public static final String AUTHORIZATION_HEADER = "Authorization";

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info(request.getMethod() + ":" + request.getRequestURL());
        if (handler instanceof HandlerMethod && !request.getMethod().equals("OPTIONS")) {
            HandlerMethod methodHandler = (HandlerMethod) handler;
            Object bean = methodHandler.getBean();
            Class<?> type = bean.getClass();
            if(type.getName().startsWith("diamond.cms.server.controllers")){
                if (type.getAnnotation(IgnoreToken.class)!=null || methodHandler.getMethodAnnotation(IgnoreToken.class)!=null) {
                    return true;
                }
                String token = request.getHeader(AUTHORIZATION_HEADER);
                Optional.ofNullable(token).orElseThrow(() -> new AuthorizationException());
                User user = userService.getByToken(token);
                Optional.ofNullable(user).orElseThrow(() -> new AuthorizationException());
                ControllerUtils.setCurrentUser(user);
            }
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
