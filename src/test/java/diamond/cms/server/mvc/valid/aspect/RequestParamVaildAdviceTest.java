package diamond.cms.server.mvc.valid.aspect;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;

import diamond.cms.server.BasicWebTest;
import diamond.cms.server.core.exceptions.Error;
public class RequestParamVaildAdviceTest extends BasicWebTest{

    @Test
    public void hibernateVaildTest() throws NoSuchMethodException, SecurityException {
        Method method = this.getClass().getMethod("vaildMethod",  String.class, String.class, String.class);
        Object [] params = new Object[]{null, "", "test"};
        Set<ConstraintViolation<RequestParamVaildAdviceTest>> validateParameters =
                RequestParamValidAspect.ValidMethodParams(this, method, params);
        Assert.assertEquals(2, validateParameters.size());
    }

    public void vaildMethod(@NotNull String name,@NotBlank String build, String test){}

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

        response = perform(post(getUrl() + "/token").param("username", "").param("password", "test"));
        Assert.assertEquals("param invalid", response.getStatus(), 400);
        node = getJsonNode(response);
        Assert.assertEquals(node.get("data").size(), 1);

        response = perform(post(getUrl() + "/token").param("username", "test").param("password", "test"));
        Assert.assertNotEquals("param valid", response.getStatus(), 400);
    }
}
