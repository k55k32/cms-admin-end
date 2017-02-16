package diamond.cms.server.mvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Tag;
import diamond.cms.server.services.TagService;

@RestController
@RequestMapping("tag")
public class TagController{

    @Autowired
    TagService tagService;


    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Tag> list(PageResult<Tag> page) {
        PageResult<Tag> list = tagService.page(page);
        return list;
    }

    @RequestMapping(value="{id}", method = RequestMethod.GET)
    public Tag list(@PathVariable String id) {
        Tag tag = tagService.get(id);
        return tag;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public List<Tag> list() {
        List<Tag> list = tagService.findAll();
        return list;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void add(Tag tag) {
        tagService.save(tag);
    }

    @RequestMapping(value="{id}", method = RequestMethod.POST)
    public void edit(Tag tag) {
        tagService.update(tag);
    }
}
