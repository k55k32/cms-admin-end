package diamond.cms.server.controllers;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.cache.CacheManager;
import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.exceptions.Error;
import diamond.cms.server.exceptions.UserNotInitException;
import diamond.cms.server.model.EmailConfig;
import diamond.cms.server.model.User;
import diamond.cms.server.services.EmailConfigService;
import diamond.cms.server.services.EmailSendService;
import diamond.cms.server.services.UserService;
import diamond.cms.server.utils.PwdUtils;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    EmailConfigService emailConfigService;

    @Resource
    EmailSendService emailSendService;

    @Resource
    CacheManager cacheManager;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    @IgnoreToken
    public Result token(@NotBlank String username, @NotBlank String password) throws IOException {
        String token = userService.login(username, PwdUtils.pwd(password));
        Result result = new Result(token);
        return result;
    }

    @RequestMapping(value="{token}", method = RequestMethod.GET)
    @IgnoreToken
    public User get(@PathVariable String token) throws AuthorizationException {
        User user = userService.getByToken(token);
        if (user == null) {
            throw new AuthorizationException();
        }
        return user;
    }
    @RequestMapping(value="logout/{token}", method = RequestMethod.GET)
    @IgnoreToken
    public void logout(@PathVariable String token){
        userService.logout(token);
    }

    @RequestMapping(value="modify", method = RequestMethod.POST)
    public User modify(@NotNull String password, HttpServletRequest request){
        return userService.modify(ControllerUtils.currentUser().getId(), PwdUtils.pwd(password));
    }

    @RequestMapping(value="need-init")
    @IgnoreToken
    public void needInit() throws UserNotInitException{
        if (!userService.isInit()){
            throw new UserNotInitException(emailConfigService.getEnbale());
        }
    }

    @RequestMapping(value="init-email-config", method = RequestMethod.GET)
    @IgnoreToken
    public EmailConfig enableConfig(){
        userService.checkoutInit();
        return emailConfigService.getEnbale().orElse(null);
    }

    @RequestMapping(value="init-step-email-config", method = RequestMethod.POST)
    @IgnoreToken
    public EmailConfig step1(@Valid EmailConfig emailConfig){
        userService.checkoutInit();

        emailConfig.setEnable(true);
        if (emailConfig.getId() != null) {
            return emailConfigService.update(emailConfig);
        }
        return emailConfigService.save(emailConfig);
    }

    @RequestMapping(value="init-send-email", method = RequestMethod.POST)
    @IgnoreToken
    public void initEmail(@NotBlank String email){
        userService.checkoutInit();
        String key = UUID.randomUUID().toString();
        cacheManager.set(email + key, key, 1000 * 60 * 60 * 2);
        emailSendService.sendEmail(email, "Init Blog Verification Code", key, key);
    }

    @RequestMapping(value="init-register", method = RequestMethod.POST)
    @IgnoreToken
    public void register (@NotBlank String username, @NotBlank String password, @NotBlank String code) {
        userService.checkoutInit();
        if (cacheManager.get(username + code) == null) {
            throw new AppException(Error.INVALID_EMAIL_CODE);
        }
        userService.register(username, PwdUtils.pwd(password));
    }
}
