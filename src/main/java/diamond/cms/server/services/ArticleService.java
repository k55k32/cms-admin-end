package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE;
import static diamond.cms.server.model.jooq.Tables.C_CATALOG;

import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Article;

@Service
public class ArticleService extends GenericService<Article>{

    @Override
    public Article update(Article entity) {
        entity.setUpdateTime(currentTime());
        return super.update(entity);
    }

    @Override
    public PageResult<Article> page(PageResult<Article> page) {
        return dao.fetch(page, e -> {
            return e.select(Fields.all(C_ARTICLE.fields(),C_CATALOG.NAME.as("catalogName")))
            .from(C_ARTICLE)
            .leftJoin(C_CATALOG).on(C_ARTICLE.CATALOG_ID.eq(C_CATALOG.ID));
        }, Article.class);
    }
}
