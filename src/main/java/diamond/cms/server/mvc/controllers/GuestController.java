package diamond.cms.server.mvc.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;

import diamond.cms.server.model.Guest;
import diamond.cms.server.mvc.annotations.IgnoreToken;
import diamond.cms.server.mvc.json.JSON;
import diamond.cms.server.services.GuestService;


@RestController
@RequestMapping("guest")
@IgnoreToken
public class GuestController {

    @Autowired
    GuestService guestService;
    
    @RequestMapping("login/github/{code}")
    public Map<String, String> loginByCode(@PathVariable String code) throws JsonProcessingException, IOException {
        String token = guestService.loginByGithub(code);
        return ImmutableMap.of("token", token);
    }
    
    @RequestMapping("{token}")
    @JSON(type = Guest.class, include = "id,email,nickname,avatar")
    public Guest getByToken(@PathVariable String token) {
        return guestService.getByToken(token);
    }
}
