package diamond.cms.server.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.core.Result;
import diamond.cms.server.json.JSON;
import diamond.cms.server.model.EmailConfig;
import diamond.cms.server.model.EmailSend;
import diamond.cms.server.services.EmailConfigService;
import diamond.cms.server.services.EmailSendService;

@RestController
@RequestMapping("email-config")
public class EmailConfigController {
    @Autowired
    EmailConfigService emailConfigService;

    @Autowired
    EmailSendService emailSendService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @JSON(type = EmailConfig.class, filter="createTime")
    public EmailConfig get(@PathVariable String id) {
        return emailConfigService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<EmailConfig> list(PageResult<EmailConfig> page) {
        PageResult<EmailConfig> list = emailConfigService.page(page);
        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(EmailConfig catalog) {
        emailConfigService.save(catalog);
        return new Result(catalog.getId());
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public void update(EmailConfig catalog) {
        emailConfigService.update(catalog);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(String id) {
        emailConfigService.delete(id);
    }

    @RequestMapping(value="change-enable", method = RequestMethod.POST)
    public void enable(String id, Boolean enable){
        emailConfigService.changeEnable(id, enable);
    }

    @RequestMapping(value="send", method = RequestMethod.POST)
    public void send(String toEmail, String title, String content, Optional<String> configId) {
        emailSendService.sendEmail(toEmail, title, content, configId, "send-test");
    }

    @RequestMapping(value="log", method = RequestMethod.GET)
    public PageResult<EmailSend> log(PageResult<EmailSend> page){
        return emailSendService.page(page);
    }

}
