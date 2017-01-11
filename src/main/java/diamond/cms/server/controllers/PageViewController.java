package diamond.cms.server.controllers;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
    public PageResult<PageView> pageview (PageResult<PageView> page, Long start, Long end) {
        return pageViewService.page(page, start, end);
    }

    @RequestMapping(value = "range-count", method = RequestMethod.GET)
    public Map<String,Long> rangeCount(Long start, Long end) {
        return pageViewService.pvRangeCount(start, end);
    }

    @RequestMapping(method = RequestMethod.POST)
    @IgnoreToken
    public void pv(@RequestBody PageView pv, HttpServletRequest request){
        pv.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pv.setIp(ControllerUtils.getIpAddr(request));
        pageViewService.save(pv);
    }


}
