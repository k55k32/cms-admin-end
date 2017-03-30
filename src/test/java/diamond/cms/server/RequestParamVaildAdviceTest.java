package diamond.cms.server.mvc.valid.aspect;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;

import diamond.cms.server.BasicWebTest;
import diamond.cms.server.core.exceptions.Error;
public class RequestParamVaildAdviceTest extends BasicWebTest{

    @Override
    protected String getUrl() {
        return "/user";
    }

    @Test
    public void tokenValidTest() throws Exception{
        MockHttpServletResponse response = perform(post(getUrl() + "/token").param("username", "").param("password", ""));
        Assert.assertEquals("param invalid", response.getStatus(), 400);
        JsonNode node = getJsonNode(response);
        Assert.assertEquals(node.get("code").asInt(), Error.INVALID_PARAMS.getCode());
        Assert.assertEquals(node.get("data").size(), 2);
        node.get("data").forEach(n -> {
            String fieldName = n.get("name").asText();
            Assert.assertTrue("username".equals(fieldName) || "password".equals(fieldName));
        });
        response = perform(post(getUrl() + "/token").param("username", "").param("password", "test"));
        Assert.assertEquals("param invalid", response.getStatus(), 400);
        node = getJsonNode(response);
        Assert.assertEquals(node.get("data").size(), 1);
        node.get("data").forEach(n -> {
            Assert.assertEquals(n.get("name").asText(), "username");
        });

        response = perform(post(getUrl() + "/token").param("username", "test").param("password", "test"));
        Assert.assertNotEquals("param valid", response.getStatus(), 400);
    }
}
