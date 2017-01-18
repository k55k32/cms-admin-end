package diamond.cms.server.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.Error;
import diamond.cms.server.model.EmailConfig;
import diamond.cms.server.model.EmailSend;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.utils.EmailUtils;

@Service
public class EmailSendService extends GenericService<EmailSend>{

    @Autowired
    EmailConfigService emailConfigService;

    public EmailSend sendEmail(String toEmail, String title, String content, String remark) {
        return sendEmail(toEmail, title, content, remark, emailConfigService.getEnbale());
    }

    public EmailSend sendEmail(String toEmail, String title, String content, String remark, Optional<EmailConfig> enbale) {
        if (!enbale.isPresent()) {
            throw new AppException(Error.NO_ENABLE_EMAIL_CONFIG);
        }
        EmailConfig config = enbale.get();
        try {
            EmailUtils.sendMessage(config.getSmtpHost(), config.getSmtpPort(), config.getSslEnable(), config.getEmailAccount(), config.getEmailPassword(), toEmail, title, content, "text/html;charset=utf-8");
        } catch (Exception e) {
            log.error("send email faild: " + e.getMessage(), e);
            throw new AppException(Error.SEND_EMAIL_FAILD, e.getMessage());
        }
        EmailSend emailSend = new EmailSend();
        emailSend.setContent(content);
        emailSend.setCreateTime(currentTime());
        emailSend.setRemark(remark);
        emailSend.setEmailConfigId(config.getId());
        emailSend.setFromEmail(config.getEmailAccount());
        emailSend.setTitle(title);
        emailSend.setToEmail(toEmail);
        save(emailSend);
        return emailSend;
    }

    public EmailSend sendEmail(String toEmail, String title, String content, Optional<String> configId, String remark) {
        if (configId.isPresent()) {
            return sendEmail(toEmail, title, content, remark, Optional.ofNullable(emailConfigService.get(configId.get())));
        }
        return sendEmail(toEmail, title, content, remark);
    }

    @Override
    public PageResult<EmailSend> page(PageResult<EmailSend> page) {
        return dao.fetch(page, Tables.C_EMAIL_SEND.CREATE_TIME.desc());
    }
}
