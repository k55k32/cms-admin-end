package diamond.cms.server.mvc.json.spring;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import diamond.cms.server.BasicWebTest;
import diamond.cms.server.model.Article;
import diamond.cms.server.services.ArticleService;

public class JsonReturnHandlerTest extends BasicWebTest{

    @Override
    protected String getUrl() {
        return "/article";
    }

    @Resource
    ArticleService articleService;

    @Test
    public void getArticle() throws UnsupportedEncodingException, Exception{
        Article article = new Article();
        article.setId("123");
        article.setTitle("heh");
        article.setContent("123123");
        article.setCreateTime(new Timestamp(System.currentTimeMillis()));
        articleService.save(article);
        MockHttpServletResponse response = perform(get(getUrl()+"/"+article.getId()));
        asserts(response);
        String result = response.getContentAsString();
        Assert.assertFalse("should not has createTime field~", hasField(result, "createTime"));
    }

    @Test
    public void getArticle2() throws UnsupportedEncodingException, Exception{
        Article article = new Article();
        article.setId("123");
        article.setTitle("heh");
        article.setContent("123123");
        article.setCreateTime(new Timestamp(System.currentTimeMillis()));
        articleService.save(article);
        MockHttpServletResponse response = perform(get(getUrl()+"/"+article.getId()));
        asserts(response);
        String result = response.getContentAsString();
        Assert.assertFalse("should not has createTime field~", hasField(result, "createTime"));
    }

}
