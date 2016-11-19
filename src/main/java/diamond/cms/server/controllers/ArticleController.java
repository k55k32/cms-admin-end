package diamond.cms.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.core.Result;
import diamond.cms.server.model.Article;
import diamond.cms.server.services.ArticleService;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @RequestMapping("page")
    public PageResult<Article> list(PageResult<Article> page) {
        return articleService.page(page);
    }

    @RequestMapping("save")
    public Result save(Article article) {
        articleService.save(article);
        return new Result(true);
    }



}
