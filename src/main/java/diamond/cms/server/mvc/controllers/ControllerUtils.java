package diamond.cms.server.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import diamond.cms.server.model.User;

public class ControllerUtils {
    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }



    public static User currentUser(){
        return CURRENT_USER.get();
    }
}
