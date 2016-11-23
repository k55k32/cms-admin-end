package diamond.cms.server.json;

import java.io.IOException;
import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import diamond.cms.server.model.Article;

public class CustomerJsonSerializerTest {

    Article article;

    @Before
    public void setArticle(){
        article = new Article();
        article.setId("1");
        article.setTitle("test");
        article.setCatalogId("id111");
        article.setCreateTime(new Timestamp(System.currentTimeMillis()));
        article.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        article.setCatalogName("hello");
    }

    @Test
    public void jsonInculdeTest() throws IOException {
        String includeNames = "id,title";
        CustomerJsonSerializer ser = new CustomerJsonSerializer();
        ser.filter(article.getClass(), includeNames, null);
        String str = ser.toJson(article);
        Assert.assertTrue("include id failed", hasField(str, "id"));
        Assert.assertFalse("should not include catalogId", hasField(str, "catalogId"));
    }

    @Test
    public void jsonfilterTest() throws IOException {
        String filter = "id,title";
        CustomerJsonSerializer ser = new CustomerJsonSerializer();
        ser.filter(article.getClass(), null, filter);
        String str = ser.toJson(article);
        Assert.assertFalse("filter id failed", hasField(str, "id"));
        Assert.assertFalse("filter title failed", hasField(str, "title"));
        Assert.assertTrue(hasField(str, "catalogId"));
    }

    private boolean hasField(String str, String string) throws JsonProcessingException, IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(str);
        return node.has(string);
    }

}
