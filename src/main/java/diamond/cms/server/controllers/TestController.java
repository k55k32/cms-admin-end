package diamond.cms.server.controllers;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.Result;
import diamond.cms.server.model.User;
import diamond.cms.server.services.UserService;

@RestController
@RequestMapping("test")
@IgnoreToken
public class TestController {

    @Resource
    UserService userService;


    @RequestMapping
    public Object test () {
        User user = userService.get("40289f515872acd1015872ad0d9a0000");
        return new Result(user);
    }

}
