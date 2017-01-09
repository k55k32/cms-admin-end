package diamond.cms.server.controllers;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.Const;
import diamond.cms.server.annotations.IgnoreToken;
import diamond.cms.server.core.PageResult;
import diamond.cms.server.json.JSON;
import diamond.cms.server.model.Comment;
import diamond.cms.server.services.CommentService;

@RestController("comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @IgnoreToken
    @JSON(type = Comment.class, filter = "ip,state,updateTime")
    public Comment saveComment(Comment comment, HttpServletRequest request){
        comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setIp(ControllerUtils.getIpAddr(request));
        comment.setState(Const.STATE_NORMAL);
        return commentService.save(comment);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Comment> commentList(PageResult<Comment> page){
        return commentService.page(page);
    }

    @RequestMapping(value = "list/{articleId}", method = RequestMethod.GET)
    @IgnoreToken
    @JSON(type = Comment.class, filter = "ip,state,updateTime")
    public PageResult<Comment> frontList(PageResult<Comment> page, @PathVariable String articleId){
        return commentService.page(page, articleId, Const.STATE_NORMAL);
    }
}
