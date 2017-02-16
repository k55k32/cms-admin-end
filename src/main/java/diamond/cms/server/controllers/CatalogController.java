package diamond.cms.server.controllers;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.PageResult;
import diamond.cms.server.core.Result;
import diamond.cms.server.json.JSON;
import diamond.cms.server.model.Catalog;
import diamond.cms.server.services.CatalogService;

@RestController
@RequestMapping("catalog")
public class CatalogController {
    @Autowired
    CatalogService catalogService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @JSON(type = Catalog.class, filter="createTime,updateTime")
    public Catalog get(@PathVariable String id) {
        return catalogService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Catalog> list(PageResult<Catalog> page) {
        PageResult<Catalog> list = catalogService.page(page);
        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(Catalog catalog) {
        catalogService.save(catalog);
        return new Result(catalog.getId());
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public void update(Catalog catalog) {
        catalog.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        catalogService.update(catalog);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@NotBlank String id) {
        catalogService.delete(id);
    }

    @RequestMapping(value="list", method = RequestMethod.GET)
    @JSON(type = Catalog.class  , include="id,name")
    public List<Catalog> findAll() {
        return catalogService.findAll();
    }

    @RequestMapping(value = "list-detail", method = RequestMethod.GET)
    @IgnoreToken
    public List<Catalog> findAllDetail () {
        return catalogService.findAllDetail();
    }
}
