package diamond.cms.server.utils;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import diamond.cms.server.model.Comment;

public class TemplateRenderUtilTest {

    @Test
    public void renderTest() {
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

    }

}
