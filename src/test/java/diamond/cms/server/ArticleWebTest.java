package diamond.cms.server;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class ArticleWebTest extends BaseWebTest{

    @Override
    String getUrl() {
        return "/article";
    }

    @Test
    public void findTest() throws Exception {
        MockHttpServletResponse response = perform(get(url).param("pageSize", "1").param("currentPage", "1"));
        asserts(response);
    }

}
