package diamond.cms.server.services;

import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Catalog;

@Service
public class CatalogService extends GenericService<Catalog>{

    @Override
    public PageResult<Catalog> page(PageResult<Catalog> page) {
        dao.fetch(page);
        return page;
    }

    @Override
    public Catalog get(String id) {
        return dao.get(id);
    };

//    private Catalog getCatalogMapper(Record m) {
//        CCatalog cataTable = CCatalog.C_CATALOG;
//        CCatalog parent = cataTable.as("parent");
//        String parentName = m.into(parent.NAME.as("parentName")).into(String.class);
//        Catalog cata = m.into(Catalog.class);
//        cata.setParentName(parentName);
//        return cata;
//    }
//
//    private SelectOnConditionStep<?> getCatalog(DSLContext e) {
//        CCatalog cataTable = CCatalog.C_CATALOG;
//        CCatalog parent = cataTable.as("parent");
//        return e.select(Fields.all(cataTable.fields(), parent.NAME.as("parentName"))).from(cataTable)
//        .leftJoin(parent).on(parent.ID.eq(cataTable.PARENT_ID));
//    }
}
