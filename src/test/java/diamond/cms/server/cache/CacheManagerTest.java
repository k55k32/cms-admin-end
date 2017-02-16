package diamond.cms.server.cache;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import diamond.cms.server.BasicTestCase;
import diamond.cms.server.core.cache.CacheManager;

public class CacheManagerTest extends BasicTestCase{

    @Resource
    CacheManager cacheManager;

    @Test
    public void test() throws InterruptedException {
        String k1 = "hello1";
        String k2 = "hello2";
        long expire = 500;
        cacheManager.set(k1, k1);
        cacheManager.set(k2, k2, expire);
        Assert.assertNotNull("cache not be null", cacheManager.get(k1));
        Assert.assertNotNull("cache not be null", cacheManager.get(k2));
        Thread.sleep(expire);
        Assert.assertNotNull("cache not be null", cacheManager.get(k1, expire));
        Assert.assertNull("cache is expire", cacheManager.get(k2));
        Thread.sleep(expire);
        Assert.assertNull("cache is expire", cacheManager.get(k1));
    }

}
