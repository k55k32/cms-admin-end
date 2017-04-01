package diamond.cms.server.mvc.aspect;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import diamond.cms.server.BasicWebTest;
import diamond.cms.server.model.Comment;

public class CommentEmailNoticeAspectTest extends BasicWebTest{

    String url = "/comment";

    @Test
    public void saveTest() throws JsonProcessingException, Exception{
        Comment comment = new Comment();
        comment.setArticleId("1");
        comment.setNickname("hello");
        comment.setEmail("");
        comment.setContent("test comment");
        MockHttpServletResponse response = perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(comment)));
        asserts(response);
    }

    @Test
    public void replyTest() throws JsonProcessingException, Exception {
        Comment comment = new Comment();
        comment.setArticleId("123");
        comment.setNickname("diamond");
        comment.setEmail("diamondfsd@gmail.com");
        comment.setContent("save comment test replyTest");
        MockHttpServletResponse response = perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(comment)));
        String id = getJsonNode(response).get("data").get("id").asText();
        Assert.assertNotNull(id);
        response = perform(post(url + "/reply/" + id).param("content", "reply: replyTest replyTest"));
        asserts(response);
        JsonNode reponseData = getJsonNode(response).get("data");
        String articleId = reponseData.get("articleId").asText();
        Assert.assertEquals(articleId, comment.getArticleId());
    }
    
}
