package diamond.cms.server;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import diamond.cms.server.core.Result;


public class CatalogTest extends BaseWebTest{


    @Test
    public void findTest() throws Exception {
        MockHttpServletResponse response = perform(get(url).param("pageSize", "1").param("currentPage", "1"));
        asserts(response);
    }


    @Test
    public void curdTest() throws Exception {
        MockHttpServletResponse response = perform(post(url).param("name", "目录1"));
        asserts(response);
        response = perform(post(url).param("name", "目录2"));
        asserts(response);
        response = perform(post(url).param("name", "目录3"));
        Result r = asserts(response);
        String updateName = "目录update";
        String id = r.getData().toString();
        response = perform(post(url + "/" + id).param("name", updateName));
        asserts(response);
        response = perform(get(url + "/" + r.getData()));
        asserts(response);
        r = asserts(response);
        Assert.assertTrue("is not update name", r.getData().toString().indexOf(updateName) > 0);
        response = perform(delete(url, id));
        asserts(response);
    }


    @Override
    String getUrl() {
        return "catalog";
    }
}
