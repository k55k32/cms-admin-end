package diamond.cms.server.mvc.controllers;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import diamond.cms.server.BasicWebTest;

public class GuestControllerTest extends BasicWebTest{

    @Test
    public void githubAuth() throws UnsupportedEncodingException, Exception {
        String content = perform(post("/guest/login/github/27d63656f95ab30a124b")).getContentAsString();
        log.info(content);
    }

}
