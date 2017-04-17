package diamond.cms.server.mvc.interceptor;

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

import diamond.cms.server.core.exceptions.AuthorizationException;
import diamond.cms.server.model.Guest;
import diamond.cms.server.mvc.annotations.CheckGuestLogin;
import diamond.cms.server.mvc.controllers.ControllerUtils;
import diamond.cms.server.services.GuestService;

@Component
public class CheckGuestLoginInterceptor implements HandlerInterceptor{

    public static final String AUTHORIZATION_HEADER = "Authorization";

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    GuestService guestService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info(request.getMethod() + ":" + request.getRequestURL());
        if (handler instanceof HandlerMethod && !request.getMethod().equals("OPTIONS")) {
            HandlerMethod methodHandler = (HandlerMethod) handler;
            Object bean = methodHandler.getBean();
            Class<?> type = bean.getClass();
            if (type.getAnnotation(CheckGuestLogin.class) != null || methodHandler.getMethodAnnotation(CheckGuestLogin.class)!=null) {
                String token = request.getHeader(AUTHORIZATION_HEADER);
                Optional.ofNullable(token).orElseThrow(() -> new AuthorizationException());
                Guest guest = guestService.getByToken(token);
                Optional.ofNullable(guest).orElseThrow(() -> new AuthorizationException());
                ControllerUtils.setCurrentGuest(guest);
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
