package diamond.cms.server.utils;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import diamond.cms.server.model.Comment;

public class TemplateRenderUtilTest {
    Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void renderTest() throws IOException {
        final String nickname = "demo-name";
        final String email = "test@email.com";
        final String temp = "hello {{nickname}} \n my email is {{email}}";
        final String renderCase = "hello " + nickname + " \n my email is "+ email;

        Map<String,String> data = ImmutableMap.of("nickname", nickname, "email", email);
        Comment comment = new Comment();
        comment.setNickname(nickname);
        comment.setEmail(email);
        String renderReuslt = TemplateRenderUtil.render(temp, data);
        Assert.assertEquals("render result must eq render case", renderCase, renderReuslt);
        String objectRenderResult = TemplateRenderUtil.render(temp, comment);
        Assert.assertEquals("render result must eq render case", renderCase, objectRenderResult);
        String fileRender = TemplateRenderUtil.renderResource("/test-template/testTemplate.html", comment);
        Assert.assertEquals(
                "<p>user "+ nullToString(comment.getNickname()) + " <" + nullToString(comment.getEmail()) + "> comment article " + 
                        nullToString(comment.getArticleTitle()) + "</p>"
                , fileRender);
    }
    
    public String nullToString(String str) {
        return str == null ? "" : str;
    }

}
