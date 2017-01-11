package diamond.cms.server.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.springframework.stereotype.Service;

import diamond.cms.server.Const;
import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Comment;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CArticle;
import diamond.cms.server.model.jooq.tables.CComment;

@Service
public class CommentService extends GenericService<Comment>{

    CComment comment = Tables.C_COMMENT;
    CArticle article = Tables.C_ARTICLE;

    public List<Comment> list(String articleId, Integer state, Optional<Long> lastTime) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(comment.ARTICLE_ID.eq(articleId));
        conditions.add(comment.STATE.eq(state));
        lastTime.ifPresent(time -> {
            conditions.add(comment.CREATE_TIME.gt(new Timestamp(time)));
        });
        return dao.fetch(conditions.stream(), comment.CREATE_TIME.desc());
    }

    public PageResult<Comment> page(PageResult<Comment> page, Optional<Integer> state, Optional<String> articleId) {
        List<Condition> conditions = new ArrayList<>();
        state.ifPresent(s ->{
            conditions.add(comment.STATE.eq(s));
        });
        articleId.ifPresent(a ->{
            conditions.add(comment.ARTICLE_ID.eq(a));
        });
        return dao.fetch(page, e -> {
            return e.select(Fields.all(comment.fields(), article.TITLE.as("articleTitle")))
            .from(comment)
            .leftJoin(article).on(comment.ARTICLE_ID.eq(article.ID))
            .where(conditions);
        }, Comment.class);
    }

    @Override
    public int delete(String id) {
        return dao.execute(e -> {
            return e.update(comment)
            .set(comment.STATE, Const.STATE_DELETE)
            .execute();
        });
    }
}
