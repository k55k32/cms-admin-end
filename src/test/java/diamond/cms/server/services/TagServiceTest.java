package diamond.cms.server.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import diamond.cms.server.BasicTestCase;
import diamond.cms.server.model.Tag;

public class TagServiceTest extends BasicTestCase{

    @Autowired
    TagService tagService;

    @Test
    public void saveTagIfNotExists() {
        Tag t1 = new Tag();
        t1.setName("tag1");
        tagService.save(t1);
        Tag t2 = new Tag();
        t2.setName("tag2");
        tagService.save(t2);
        Tag t3 = new Tag();
        t3.setName("test3");
        List<Tag> tags = tagService.saveTagIfNotExists(new String[]{t1.getId(), t2.getId(), t3.getName(), t3.getName(), t1.getName()});
        boolean hasNames = tags.stream().map(Tag::getName).collect(Collectors.toList()).containsAll(Arrays.asList(new String[] {
                t1.getName(), t2.getName(), t3.getName()
        }));
        Assert.assertTrue("hasNames", hasNames);
        Assert.assertTrue("tags length not 3 == " + tags.size() , tags.size() == 3);
    }

}
