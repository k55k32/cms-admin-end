package diamond.cms.server.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.model.Article;

@RestController
@RequestMapping("article")
public class ArticleController {

    @RequestMapping("list")
    public List<Article> list() {
        return null;
    }

}
