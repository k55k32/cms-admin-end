package diamond.cms.server.services;

import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.PageView;
import diamond.cms.server.model.jooq.Tables;

@Service
public class PageViewService extends GenericService<PageView>{

    @Override
    public PageResult<PageView> page(PageResult<PageView> page) {
        return dao.fetch(page, Tables.C_PAGE_VIEW.CREATE_TIME.desc());
    }
}
