package diamond.cms.server.controllers;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.model.User;
import diamond.cms.server.services.UserService;
import diamond.cms.server.utils.PwdUtils;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    @IgnoreToken
    public Result token(String username, String password) throws IOException {
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

    @RequestMapping(value="modify")
    public User modify(String password, HttpServletRequest request){
        return userService.modify(ControllerUtils.currentUser().getId(), PwdUtils.pwd(password));
    }
}
