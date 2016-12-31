package diamond.cms.server.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.model.PageView;
import diamond.cms.server.services.PageViewService;

@RestController
@RequestMapping("pv")
public class PageViewController {

    @Autowired
    PageViewService pageViewService;

    @RequestMapping(method = RequestMethod.POST)
    @IgnoreToken
    public void pv(@RequestBody PageView pv){
        pv.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pageViewService.save(pv);
    }
}
