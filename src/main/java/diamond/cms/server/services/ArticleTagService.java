package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE_TAG;
import static diamond.cms.server.model.jooq.Tables.C_TAG;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.ArticleTag;
import diamond.cms.server.model.Tag;
import diamond.cms.server.model.jooq.Tables;
@Service
public class ArticleTagService extends GenericService<ArticleTag>{


    public List<ArticleTag> findTagIds(String articleId) {
        return dao.fetch(Tables.C_ARTICLE_TAG.ARTICLE_ID.eq(articleId));
    }

    public void deleteByArticleId(String articleId) {
        dao.delete(Tables.C_ARTICLE_TAG.ARTICLE_ID.eq(articleId));
    }

    public List<ArticleTag> insert(List<ArticleTag> tagList) {
        dao.insert(tagList);
        return tagList;
    }

    public List<ArticleTag> findTags(Collection<String> articleIds) {
        List<ArticleTag> tags = dao.execute(e -> {
            return e.select(Fields.all(C_TAG.fields(), C_ARTICLE_TAG.fields())).from(C_ARTICLE_TAG)
            .leftJoin(C_TAG).on(C_TAG.ID.eq(C_ARTICLE_TAG.TAG_ID))
            .where(C_ARTICLE_TAG.ARTICLE_ID.in(articleIds))
            .fetch(r -> {
                ArticleTag articleTag =  r.into(ArticleTag.class);
                articleTag.setTag(r.into(Tag.class));
                return articleTag;
            });
        });
        return tags;
    }

    public List<Tag> findTags(String articleId) {
        return dao.execute(e -> {
            return e.select(C_TAG.fields()).from(C_TAG)
            .leftJoin(C_ARTICLE_TAG).on(C_ARTICLE_TAG.TAG_ID.eq(C_TAG.ID))
            .where(C_ARTICLE_TAG.ARTICLE_ID.eq(articleId))
            .fetchInto(Tag.class);
        });
    }
}
