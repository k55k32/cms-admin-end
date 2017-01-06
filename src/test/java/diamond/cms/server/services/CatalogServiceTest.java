package diamond.cms.server.services;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import diamond.cms.server.BasicTestCase;
import diamond.cms.server.model.Catalog;

public class CatalogServiceTest extends BasicTestCase{

    @Resource
    CatalogService catalogService;

    @Test
    public void testAllDetail(){
        List<Catalog> catalogs = catalogService.findAllDetail();
        Assert.assertNotNull(catalogs);
    }

}
