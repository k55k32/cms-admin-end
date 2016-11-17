package diamond.cms.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("menu")
public class MenuController {

    @RequestMapping
    public String menus () {
        return "hello world";
    }

}
