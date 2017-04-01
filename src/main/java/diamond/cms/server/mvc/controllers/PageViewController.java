package diamond.cms.server.mvc.controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.IpLocation;
import diamond.cms.server.model.PageView;
import diamond.cms.server.mvc.annotations.IgnoreToken;
import diamond.cms.server.services.IpLocationService;
import diamond.cms.server.services.PageViewService;

@RestController
@RequestMapping("pv")
public class PageViewController {

    @Autowired
    PageViewService pageViewService;

    @Autowired
    IpLocationService ipLocationService;

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<PageView> pageview (PageResult<PageView> page, Long start, Long end) {
        page =  pageViewService.page(page, start, end);
        Map<String, String> ipLocMap = ipLocationService.saveOrList(page.getData().stream().map(PageView::getIp)
                .collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(IpLocation::getIp, IpLocation::getLocation));
        page.getData().forEach(p -> {
            String loc = ipLocMap.get(p.getIp());
            p.setLocation(loc);
        });
        return page;
    }

    @RequestMapping(value = "pv-count", method = RequestMethod.GET)
    public List<PageView> pvCount(Long start, Long end) {
        return pageViewService.findByTime(start, end);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @IgnoreToken
    public void pv(@RequestBody PageView pv, HttpServletRequest request){
        pv.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pv.setIp(ControllerUtils.getIpAddr(request));
        ipLocationService.getOrSave(pv.getId());
        pageViewService.save(pv);
    }


}
