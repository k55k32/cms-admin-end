package diamond.cms.server.utils;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class IpUtilTest {

    @Test
    public void test() throws JsonProcessingException, IOException {
        IpUtil.getLocation("14.149.42.62");
    }

}
