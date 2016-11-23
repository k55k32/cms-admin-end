package diamond.cms.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.json.JSON;
import diamond.cms.server.model.Article;
import diamond.cms.server.services.ArticleService;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @JSON(type = Article.class, filter="createTime,updateTime")
    public Article get(@PathVariable String id) {
        return articleService.get(id);
    }
    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Article> list(PageResult<Article> page) {
        PageResult<Article> list = articleService.page(page);
        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public boolean save(Article article) {
        articleService.save(article);
        return true;
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public boolean update(@PathVariable String id, String title, String content, String catalogId) {
        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setContent(content);
        article.setCatalogId(catalogId);
        articleService.update(article);
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(String id) {
        articleService.delete(id);
        return true;
    }


}
