package diamond.cms.server.mvc.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Setting;
import diamond.cms.server.mvc.annotations.IgnoreToken;
import diamond.cms.server.services.SettingService;

@RestController
@RequestMapping("setting")
public class SettingController {
    @Autowired
    SettingService settingService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Setting get(@PathVariable String id) {
        return settingService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Setting> list(PageResult<Setting> page) {
        PageResult<Setting> list = settingService.page(page);
        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void save(Setting setting) {
        settingService.save(setting);
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public void update(Setting setting) {
        settingService.update(setting);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(String id) {
        settingService.delete(id);
    }

    @RequestMapping(value="list", method = RequestMethod.GET)
    @IgnoreToken
    public Map<String,String> findAll() {
        return settingService.findMap();
    }
}
