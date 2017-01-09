package diamond.cms.server.services;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Comment;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CComment;

@Service
public class CommentService extends GenericService<Comment>{

    public PageResult<Comment> page(PageResult<Comment> page, String articleId, Integer state) {
        CComment comment = Tables.C_COMMENT;
        return dao.fetch(page, Stream.of(comment.ARTICLE_ID.eq(articleId).and(comment.STATE.eq(state))), comment.CREATE_TIME.asc());
    }

    @Override
    public PageResult<Comment> page(PageResult<Comment> page) {
        return dao.fetch(page, Tables.C_COMMENT.CREATE_TIME.desc());
    }
}
