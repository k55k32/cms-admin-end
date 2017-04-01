package diamond.cms.server.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jooq.Condition;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.Fields;
import diamond.cms.server.model.Comment;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CArticle;
import diamond.cms.server.model.jooq.tables.CComment;
import diamond.cms.server.mvc.Const;

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
            .where(conditions).orderBy(comment.CREATE_TIME.desc());
        }, Comment.class);
    }

    @Override
    public int delete(String id) {
        return updateState(id, Const.STATE_DELETE);
    }

    private Integer updateState(String id, int state) {
        return dao.execute(e -> {
            return e.update(comment)
            .set(comment.STATE, state)
            .where(comment.ID.eq(id))
            .execute();
        });

    }

    public Integer recovery(String id) {
        return updateState(id, Const.STATE_NORMAL);
    }

    public Comment saveNewComment(Comment comment) {
        comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setState(Const.STATE_NORMAL);
        super.save(comment);
        return comment;
    }

    public Comment reply(Comment comment) {
        String replyId = comment.getReplyId();
        String articleId = dao.get(replyId).getArticleId();
        comment.setArticleId(articleId);
        return this.saveNewComment(comment);
    }
}
