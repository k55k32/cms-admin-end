package diamond.cms.server.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Comment;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CComment;

@Service
public class CommentService extends GenericService<Comment>{

    public List<Comment> list(String articleId, Integer state, Optional<Long> lastTime) {
        CComment comment = Tables.C_COMMENT;
        List<Condition> conditions = new ArrayList<>();
        conditions.add(comment.ARTICLE_ID.eq(articleId));
        conditions.add(comment.STATE.eq(state));
        lastTime.ifPresent(time -> {
            conditions.add(comment.CREATE_TIME.gt(new Timestamp(time)));
        });
        return dao.fetch(conditions.stream(), comment.CREATE_TIME.desc());
    }

    @Override
    public PageResult<Comment> page(PageResult<Comment> page) {
        return dao.fetch(page, Tables.C_COMMENT.CREATE_TIME.desc());
    }
}
