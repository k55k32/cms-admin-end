package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE;
import static diamond.cms.server.model.jooq.Tables.C_CATALOG;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Article;
import diamond.cms.server.model.ArticleDetail;
import diamond.cms.server.model.jooq.tables.CArticle;

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
            .where(C_ARTICLE.STATUS.in(Arrays.asList(status)))
            .orderBy(C_ARTICLE.CREATE_TIME.desc());
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

    public ArticleDetail getDetail(String id) {
        CArticle article = C_ARTICLE.as("a");
        CArticle before = C_ARTICLE.as("b");
        CArticle next = C_ARTICLE.as("n");
        CArticle inner = C_ARTICLE.as("i");
        ArticleDetail a = dao.execute(e -> {
            return e.select(Fields.all(article.fields(),
                before.ID.as("beforeId"),
                before.TITLE.as("beforeTitle"),
                next.ID.as("nextId"),
                next.TITLE.as("nextTitle")
                )).from(article)
                .leftJoin(before).on(before.ID.eq(e.select(inner.ID).from(inner).where(article.CREATE_TIME.ge(inner.CREATE_TIME)).and(inner.ID.ne(article.ID)).orderBy(inner.CREATE_TIME.desc()).limit(0, 1)))
                .leftJoin(next).on((next.ID.eq(e.select(inner.ID).from(inner).where(article.CREATE_TIME.le(inner.CREATE_TIME)).and(inner.ID.ne(article.ID)).orderBy(inner.CREATE_TIME).limit(0, 1))))
                .where(article.ID.eq(id))
                .fetchOne(r -> {
                    return dao.mapperEntityEx(r, ArticleDetail.class);
                });
        });
        return a;
    }

}
