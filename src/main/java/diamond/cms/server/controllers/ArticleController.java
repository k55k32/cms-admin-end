package diamond.cms.server.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.PageResult;
import diamond.cms.server.json.JSON;
import diamond.cms.server.model.Article;
import diamond.cms.server.model.ArticleDetail;
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

    @RequestMapping(value = "save/draft", method = RequestMethod.POST)
    @JSON(type = Article.class, filter="createTime,updateTime")
    public Article saveDraft(@RequestBody Article article) {
        article = articleService.saveDraft(article);
        return article;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@RequestBody Article article) {
        article.setStatus(Article.STATUS_PUBLISH);
        articleService.save(article);
        return article.getId();
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public void update(@RequestBody Article article) {
        article.setStatus(Article.STATUS_PUBLISH);
        articleService.update(article);
    }

    @RequestMapping(value="unpublish/{id}", method = RequestMethod.POST)
    public void unpublish(@PathVariable String id) {
        articleService.updateStatus(id, Article.STATUS_UNPUBLISH);
    }

    @RequestMapping(value="publish/{id}", method = RequestMethod.POST)
    public void publish(@PathVariable String id) {
        articleService.updateStatus(id, Article.STATUS_PUBLISH);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(String id) {
        articleService.delete(id);
        return true;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @IgnoreToken
    @JSON(type=Article.class, filter="status,content")
    public PageResult<Article> listPage(PageResult<Article> page, String catalogId) {
        PageResult<Article> articles = articleService.page(page, Article.STATUS_PUBLISH, Optional.ofNullable(catalogId));
        return articles;
    }

    @RequestMapping(value = "createtime/{id}", method = RequestMethod.POST)
    public void changeCreateTime(@PathVariable String id, Long time) {
        articleService.updateCreateTime(id, time);
    }

    @RequestMapping(value="detail/{id}", method = RequestMethod.GET)
    @IgnoreToken
    public ArticleDetail detail(@PathVariable String id) {
        return articleService.getDetail(id);
    }

    @RequestMapping(value="id-title", method = RequestMethod.GET)
    @JSON(type=Article.class, include="id,title")
    public List<Article> idTitle() {
        return articleService.findIdTitle();
    }
}
