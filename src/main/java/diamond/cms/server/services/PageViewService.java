package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_PAGE_VIEW;

import java.sql.Timestamp;
import java.util.Map;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.PageView;

@Service
public class PageViewService extends GenericService<PageView>{

    @Autowired
    IpLocationService ipLocationService;

    public PageResult<PageView> page(PageResult<PageView> page, Long start, Long end) {
        page = dao.fetch(page,getCondition(start, end), C_PAGE_VIEW.CREATE_TIME.desc());
        return page;
    }

    private Stream<Condition> getCondition(Long start, Long end) {
        return Stream.of(C_PAGE_VIEW.CREATE_TIME.ge(new Timestamp(start)), C_PAGE_VIEW.CREATE_TIME.le(new Timestamp(end)));
    }

    public Map<String, Long> pvRangeCount(Long start, Long end) {
        @SuppressWarnings("unchecked")
        Map<String, Long> map = (Map<String, Long>) dao.execute(e -> {
            return e.fetch("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') createTime, COUNT(1) countPv FROM c_page_view GROUP BY DATE_FORMAT(create_time, '%Y%m%d')")
            .intoMap("createTime", "countPv");
        });
        return map;
    }
}
