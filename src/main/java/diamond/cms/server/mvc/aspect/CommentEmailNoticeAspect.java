package diamond.cms.server.mvc.aspect;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import diamond.cms.server.model.Comment;
import diamond.cms.server.model.User;
import diamond.cms.server.services.ArticleService;
import diamond.cms.server.services.CommentService;
import diamond.cms.server.services.EmailSendService;
import diamond.cms.server.services.UserService;
import diamond.cms.server.utils.TemplateRenderUtil;
import diamond.cms.server.utils.ValidateUtils;

@Component
@Aspect
public class CommentEmailNoticeAspect{

    public static String COMMENT_NOTICE_TEMP = "/email-template/CommentNoticeTemplate.html";
    public static String REPLY_NOTICE_TEMP =  "/email-template/ReplyCommentNoticeTemplate.html";
    
    @Resource
    UserService userService;
    @Resource
    EmailSendService emailSendService;
    @Resource
    ArticleService articleService;
    @Resource
    CommentService commentService;

    Logger log = LoggerFactory.getLogger(getClass());

    @AfterReturning(returning="comment", pointcut="execution(* diamond.cms.server.mvc.controllers.CommentController.saveComment(..))")
    public void after(Comment comment) {
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    User admin = userService.findAdmin();
                    if (admin != null) {
                        String artTitle = articleService.getTitle(comment.getArticleId());
                        comment.setArticleTitle(artTitle);
                        try {
                            String emailContent = TemplateRenderUtil.renderResource(COMMENT_NOTICE_TEMP, comment);
                            emailSendService.sendEmail(admin.getUsername(), "Blog Comment Notice", emailContent, "comment-notice-" + comment.getId());
                        } catch (IOException e) {
                            log.error("template render error, send email after comment faild", e);
                        }
                    }
                } catch(Exception e) {
                    log.error("send comment notice email faild", e);
                }
            }
        });
    }
    
    @AfterReturning(returning="comment", pointcut="execution(* diamond.cms.server.mvc.controllers.CommentController.replyComment(..))")
    public void afterReply(Comment comment) {
        try {
            Comment byReplyComment = commentService.get(comment.getReplyId());
            String toEmail = byReplyComment.getEmail();
            if (ValidateUtils.isEmail(toEmail)) {
                String articleTitle = articleService.getTitle(comment.getArticleId());
                comment.setArticleTitle(articleTitle);
                try {
                    String emailContent = TemplateRenderUtil.renderResource(REPLY_NOTICE_TEMP, comment);
                    emailSendService.sendEmail(toEmail, "Comment Reply Notice", emailContent, "comment-reply-" + comment.getId());
                } catch (IOException e) {
                    log.error("template render error, send email reply comment faild", e);
                }
            }
        } catch (Exception e) {
            log.error("send comment reply email faild", e);
        }
    }
}
