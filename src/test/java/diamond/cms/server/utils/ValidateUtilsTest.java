package diamond.cms.server.utils;

import org.junit.Assert;
import org.junit.Test;

public class ValidateUtilsTest {

    @Test
    public void isEmailTest() {
        Assert.assertTrue(ValidateUtils.isEmail("test@qq.com"));
        Assert.assertTrue(ValidateUtils.isEmail("test@qq.com.cn"));
        Assert.assertTrue(ValidateUtils.isEmail("test_hjello@qq.com"));
        Assert.assertTrue(ValidateUtils.isEmail("test_dd_dd@qq.com"));
        Assert.assertTrue(ValidateUtils.isEmail("test123123_ddd@qq.com.net"));
        
        Assert.assertFalse(ValidateUtils.isEmail(null));
        Assert.assertFalse(ValidateUtils.isEmail(""));
        Assert.assertFalse(ValidateUtils.isEmail("test@.com"));
        Assert.assertFalse(ValidateUtils.isEmail("test@ddd12--..com"));
        Assert.assertFalse(ValidateUtils.isEmail("test@com..com"));
        Assert.assertFalse(ValidateUtils.isEmail("test@.com.cn"));
        Assert.assertFalse(ValidateUtils.isEmail("test@qq.com.net."));
    }

}
