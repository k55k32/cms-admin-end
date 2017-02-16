package diamond.cms.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import diamond.cms.server.core.Result;
import diamond.cms.server.mvc.interceptor.AuthorizationInterceptor;
import diamond.cms.server.services.UserService;
import diamond.cms.server.utils.PwdUtils;

public abstract class BasicWebTest extends BasicTestCase{
    @Autowired
    WebApplicationContext webApplicationConnect;
    @Autowired
    UserService userService;
    MockMvc mvc;
    String token = "";
    String url;

    protected abstract String getUrl();

    @Before
    public void setUp() throws Exception {
        url = getUrl();
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        log.info("url", url);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
        String login = "/user/token";
        String username = "test_user" + System.currentTimeMillis();
        String password = "test_password";
        userService.register(username, PwdUtils.pwd(password));
        String resultStr = mvc.perform(post(login).param("username", username).param("password", password)).andReturn()
                .getResponse().getContentAsString();
        ObjectMapper m = new ObjectMapper();
        Result r = m.readValue(resultStr, Result.class);
        token = (String) r.getData();
        log.info("init token:" + token);
    }

    public MockHttpServletRequestBuilder post(String url) {
        return MockMvcRequestBuilders.post(url);
    }

    public MockHttpServletRequestBuilder get(String uri) {
        return MockMvcRequestBuilders.get(uri);
    }

    public MockHttpServletRequestBuilder delete(String url, String id) {
        return MockMvcRequestBuilders.delete(url).param("id", id);
    }

    public MockHttpServletRequestBuilder upload(String url, File file) throws IOException {
        return MockMvcRequestBuilders.fileUpload(url).file(new MockMultipartFile("file", new FileInputStream(file)));
    }

    public MockHttpServletResponse perform(MockHttpServletRequestBuilder param) throws Exception {
        return mvc.perform(param
                .header(AuthorizationInterceptor.AUTHORIZATION_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
    }

    public Result asserts(MockHttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
        String result = response.getContentAsString();
        log.info("result: " + result);
        Assert.assertTrue(response.getStatus() == 200);
        Assert.assertTrue(result.length() > 0);
        ObjectMapper m = new ObjectMapper();
        Result r = m.readValue(result, Result.class);
        Assert.assertTrue("result is not success", r.isSuccess());
        return r;
    }

    public void assertsString(MockHttpServletResponse response) throws UnsupportedEncodingException {
        String result = response.getContentAsString();
        Assert.assertTrue(response.getStatus() == 200);
        Assert.assertTrue(result.length() > 0);
        log.info("assert string:" + result);
    }

    public boolean hasField(String str, String string) throws JsonProcessingException, IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(str);
        return node.has(string) || node.get("data").has(string);
    }
}