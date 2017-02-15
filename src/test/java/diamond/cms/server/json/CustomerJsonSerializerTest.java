package diamond.cms.server.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import diamond.cms.server.model.Article;
import diamond.cms.server.model.Tag;

public class CustomerJsonSerializerTest {

    Article article;
    ObjectMapper om = new ObjectMapper();

    @Before
    public void setArticle(){
        article = new Article();
        article.setId("1");
        article.setTitle("test");
        article.setCatalogId("id111");
        article.setCreateTime(new Timestamp(System.currentTimeMillis()));
        article.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        article.setCatalogName("hello");
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tag t = new Tag();
            t.setId("ID-" + i);
            t.setName("NAME-" + i);
            t.setArticleCount(i);
            tags.add(t);
        }
        article.setTags(tags);

    }

    @Test
    public void jsonInculdeTest() throws IOException {
        String includeNames = "id,title";
        CustomerJsonSerializer ser = new CustomerJsonSerializer();
        ser.filter(article.getClass(), includeNames, null);
        String str = ser.toJson(article);
        Assert.assertTrue("include id failed", hasField(str, "id"));
        Assert.assertFalse("should not include catalogId", hasField(str, "catalogId"));
        Assert.assertNull(om.readValue(str, Article.class).getTags());
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
        Assert.assertNotNull(om.readValue(str, Article.class).getTags());
    }

    @Test
    public void jsonsFilter() throws IOException {
        String filter = "id,title,tags";
        CustomerJsonSerializer ser = new CustomerJsonSerializer();
        ser.filter(Article.class, filter, null);
        ser.filter(Tag.class, "id", null);
        String str = ser.toJson(article);
        Assert.assertTrue(hasField(str, "id"));
        Article a = om.readValue(str, Article.class);
        a.getTags().forEach(t -> {
            Assert.assertNotNull(t.getId());
            Assert.assertNull(t.getName());
        });
    }

    private boolean hasField(String str, String string) throws JsonProcessingException, IOException {
        JsonNode node = om.readTree(str);
        return node.has(string);
    }

}
