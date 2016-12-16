package diamond.cms.server.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import diamond.cms.server.model.Tag;
import diamond.cms.server.model.jooq.Tables;

@Service
public class TagService extends GenericService<Tag>{

    public List<Tag> saveTagIfNotExists(String[] tagIds) {
        List<Tag> tags = dao.fetch(Tables.C_TAG.ID.in(tagIds).or(Tables.C_TAG.NAME.in(tagIds)));
        Map<String,String> nameMap = tags.stream().collect(Collectors.toMap(Tag::getId, Tag::getName));
        List<Tag> newTags = new ArrayList<>();
        new HashSet<String>(Arrays.asList(tagIds)).forEach(idOrName -> {
            if (!nameMap.keySet().contains(idOrName) && !nameMap.values().contains(idOrName)) {
                Tag tag = new Tag();
                tag.setName(idOrName);
                tag.setId(generateID());
                newTags.add(tag);
                tags.add(tag);
            }
        });
        if (!newTags.isEmpty()) dao.insert(newTags);
        return tags;
    }
}
