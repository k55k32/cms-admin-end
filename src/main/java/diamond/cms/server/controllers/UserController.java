package diamond.cms.server.controllers;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.Result;
import diamond.cms.server.exceptions.AuthorizationException;
import diamond.cms.server.model.User;
import diamond.cms.server.services.UserService;

@RestController
@RequestMapping("user")
@IgnoreToken
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public Result token(User user) throws IOException {
        String token = userService.login(user.getUsername(), user.getPassword());
        Result result = new Result(token);
        return result;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Result register(User user) {
        String token = userService.register(user.getUsername(), user.getPassword());
        return new Result(token);
    }

    @RequestMapping(value="{token}", method = RequestMethod.GET)
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
}
