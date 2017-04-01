package diamond.cms.server.mvc.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.model.Comment;
import diamond.cms.server.model.User;
import diamond.cms.server.mvc.Const;
import diamond.cms.server.mvc.annotations.IgnoreToken;
import diamond.cms.server.mvc.json.JSON;
import diamond.cms.server.services.CommentService;

@RestController()
@RequestMapping(value = "comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    @IgnoreToken
    @JSON(type = Comment.class, filter = "ip,state,updateTime")
    public Comment saveComment(@RequestBody Comment comment, HttpServletRequest request){
        comment.setIp(ControllerUtils.getIpAddr(request));
        comment.setFromAuthor(false);
        return commentService.saveNewComment(comment);
    }


    @RequestMapping(value = "{articleId}", method = RequestMethod.GET)
    @IgnoreToken
    @JSON(type = Comment.class, include = "id,nickname,createTime,content")
    public List<Comment> frontList(@PathVariable String articleId, Optional<Long> lastTime){
        return commentService.list(articleId, Const.STATE_NORMAL, lastTime);
    }

    @RequestMapping(value = "reply", method = RequestMethod.POST)
    public Comment replyComment(String content, String replyId, HttpServletRequest request) {
        User user = ControllerUtils.currentUser();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setReplyId(replyId);
        comment.setEmail(user.getUsername());
        comment.setNickname("Author");
        comment.setIp(ControllerUtils.getIpAddr(request));
        comment.setFromAuthor(true);
        commentService.reply(comment);
        return comment;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Comment> commentList(PageResult<Comment> page, Optional<Integer> state, Optional<String> articleId){
        return commentService.page(page, state, articleId);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(String id){
        commentService.delete(id);
    }

    @RequestMapping(value = "recovery/{id}", method = RequestMethod.GET)
    public void recovery(@PathVariable String id) {
        commentService.recovery(id);
    }
}
