package diamond.cms.server;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import diamond.cms.server.core.Result;
import diamond.cms.server.interceptor.AuthorizationInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@Rollback
@Transactional
@WebAppConfiguration
public abstract class BaseTestCase {
    @Autowired
    WebApplicationContext webApplicationConnect;
    MockMvc mvc;
    String token;
    String url;
    abstract String getUrl();
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Before
    public void setUp() throws Exception {
        url = getUrl();
        if (!url.startsWith("/")) {
            url = "/" + url;
        }
        log.info("url", url);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
        String login = "/user/token";
        String resultStr  = mvc.perform(post(login).param("username", "123123").param("password", "123123")).andReturn()
        .getResponse().getContentAsString();
        ObjectMapper m = new ObjectMapper();
        Result r = m.readValue(resultStr, Result.class);
        token = (String) r.getData();
        log.info("init token:" + token);
    }
     MockHttpServletRequestBuilder post(String url){
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(url);
        if (token != null) {
            builder = builder.header(AuthorizationInterceptor.AUTHORIZATION_HEADER, token);
        }
        return builder;
    }
     MockHttpServletRequestBuilder get(String uri) {
        return MockMvcRequestBuilders.get(uri).header(AuthorizationInterceptor.AUTHORIZATION_HEADER, token).accept(MediaType.APPLICATION_JSON_UTF8);
    }
     MockHttpServletRequestBuilder delete(String url, String id) {
        return MockMvcRequestBuilders.delete(url).param("id", id).header(AuthorizationInterceptor.AUTHORIZATION_HEADER, token).accept(MediaType.APPLICATION_JSON_UTF8);
    }
     MockHttpServletResponse perform(MockHttpServletRequestBuilder param) throws Exception {
        return mvc.perform(param).andReturn().getResponse();
    }

    Result asserts(MockHttpServletResponse response) throws JsonParseException, JsonMappingException, IOException{
        String result = response.getContentAsString();
        log.info("result: " + result);
        Assert.assertTrue(response.getStatus()==200);
        Assert.assertTrue(result.length() > 0);
        ObjectMapper m = new ObjectMapper();
        Result r = m.readValue(result,Result.class);
        Assert.assertTrue("result is not success", r.isSuccess());
        return r;
    }
}