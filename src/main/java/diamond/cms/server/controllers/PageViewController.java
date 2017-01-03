package diamond.cms.server.controllers;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.PageView;
import diamond.cms.server.services.PageViewService;

@RestController
@RequestMapping("pv")
public class PageViewController {

    @Autowired
    PageViewService pageViewService;


    @RequestMapping(method = RequestMethod.GET)
    public PageResult<PageView> pageview (PageResult<PageView> page) {
        return pageViewService.page(page);
    }

    @RequestMapping(method = RequestMethod.POST)
    @IgnoreToken
    public void pv(@RequestBody PageView pv, HttpServletRequest request){
        pv.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pv.setIp(getIpAddr(request));
        pageViewService.save(pv);
    }

    String getIpAddr(HttpServletRequest request) {
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
}
