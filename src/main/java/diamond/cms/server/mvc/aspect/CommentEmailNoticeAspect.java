package diamond.cms.server.mvc.aspect;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import diamond.cms.server.model.Comment;
import diamond.cms.server.model.User;
import diamond.cms.server.services.EmailSendService;
import diamond.cms.server.services.UserService;

@Component
@Aspect
public class CommentEmailNoticeAspect{

    @Resource
    UserService userService;

    Logger log = LoggerFactory.getLogger(getClass());
    @Pointcut("execution(* diamond.cms.server.mvc.controllers.CommentController.saveComment(..))")
    public void controllerAfter(){};

    @AfterReturning(returning="comment", pointcut="execution(* diamond.cms.server.mvc.controllers.CommentController.saveComment(..))")
    public void after(Comment comment) {
        log.info("save comment");
        User admin = userService.findAdmin();
        if (admin != null) {
            CompletableFuture.runAsync(new Runnable() {
                @Resource
                EmailSendService emailSendService;
                @Override
                public void run() {
                    emailSendService.sendEmail(admin.getUsername(), "Blog Comment Notice", "User Comment \n nickname: %s, ", "comment-notice");
                }
            });
        }
    }
}
