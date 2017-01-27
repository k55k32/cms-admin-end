package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_ARTICLE;
import static diamond.cms.server.model.jooq.Tables.C_CATALOG;

import java.util.List;

import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Article;
import diamond.cms.server.model.Catalog;
import diamond.cms.server.model.jooq.Tables;


@Service
public class CatalogService extends GenericService<Catalog>{

    @Override
    public PageResult<Catalog> page(PageResult<Catalog> page) {
        dao.fetch(page, Tables.C_CATALOG.SORT.asc());
        return page;
    }

    @Override
    public Catalog get(String id) {
        return dao.get(id);
    }

    public List<Catalog> findAllDetail() {
        return dao.execute(e -> {
            return e.select(Fields.all(C_CATALOG.fields(), DSL.count(C_ARTICLE.ID).as("articleCount"))).from(C_ARTICLE)
            .leftJoin(C_CATALOG).on(C_CATALOG.ID.eq(C_ARTICLE.CATALOG_ID))
            .where(C_ARTICLE.STATUS.eq(Article.STATUS_PUBLISH))
            .groupBy(C_CATALOG.ID)
            .orderBy(C_CATALOG.SORT).fetch(r -> {
                return dao.mapperEntityEx(r, Catalog.class);
            });
        });
    }
}
