package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE;
import static diamond.cms.server.model.jooq.Tables.C_CATALOG;

import java.util.Arrays;

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
        return page(page, Article.STATUS_PUBLISH, Article.STATUS_UNPUBLISH);
    }

    @Override
    public String delete(String id) {
        updateStatus(id, Article.STATUS_DELETE);
        return id;
    }

    public void updateStatus(String id, int status) {
        dao.execute(e -> {
           return e.update(C_ARTICLE).set(C_ARTICLE.STATUS, status)
           .where(C_ARTICLE.ID.eq(id))
           .execute();
        });
    }


    public PageResult<Article> page(PageResult<Article> page, Integer...status) {
        return dao.fetch(page, e -> {
            return e.select(Fields.all(C_ARTICLE.fields(),C_CATALOG.NAME.as("catalogName")))
            .from(C_ARTICLE)
            .leftJoin(C_CATALOG).on(C_ARTICLE.CATALOG_ID.eq(C_CATALOG.ID))
            .where(C_ARTICLE.STATUS.in(Arrays.asList(status)));
        }, Article.class);
    }

    public Article saveDraft(Article article) {
        if (article.getId() == null) {
            article = save(article);
        } else {
            article = update(article);
        }
        return article;
    }

}
