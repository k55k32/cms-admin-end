package diamond.cms.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.core.Result;
import diamond.cms.server.model.Catalog;
import diamond.cms.server.services.CatalogService;

@RestController
@RequestMapping("catalog")
public class CatalogController {
    @Autowired
    CatalogService catalogService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @JsonIgnoreProperties("createTime, updateTime")
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
    public boolean update(@PathVariable String id, String name, String parentId) {
        Catalog catalog = new Catalog();
        catalog.setId(id);
        catalog.setName(name);
        catalogService.update(catalog);
        return true;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(String id) {
        catalogService.delete(id);
        return true;
    }

    @RequestMapping(value="list", method = RequestMethod.GET)
    public List<Catalog> findAll() {
        return catalogService.findAll();
    }

}
