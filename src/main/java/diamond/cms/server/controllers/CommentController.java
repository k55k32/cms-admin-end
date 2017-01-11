package diamond.cms.server.controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

@RestController()
@RequestMapping(value = "comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @IgnoreToken
    @JSON(type = Comment.class, filter = "ip,state,updateTime")
    public Comment saveComment(@RequestBody Comment comment, HttpServletRequest request){
        comment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setIp(ControllerUtils.getIpAddr(request));
        comment.setState(Const.STATE_NORMAL);
        return commentService.save(comment);
    }


    @RequestMapping(value = "{articleId}", method = RequestMethod.GET)
    @IgnoreToken
    @JSON(type = Comment.class, include = "id,nickname,createTime,content")
    public List<Comment> frontList(@PathVariable String articleId, Optional<Long> lastTime){
        return commentService.list(articleId, Const.STATE_NORMAL, lastTime);
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageResult<Comment> commentList(PageResult<Comment> page, Optional<Integer> state, Optional<String> articleId){
        return commentService.page(page, state, articleId);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id){
        commentService.delete(id);
    }
}
