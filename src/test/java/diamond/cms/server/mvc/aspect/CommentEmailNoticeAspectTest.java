package diamond.cms.server.mvc.aspect;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

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

}
