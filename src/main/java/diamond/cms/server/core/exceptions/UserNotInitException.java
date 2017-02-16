package diamond.cms.server.core.exceptions;

import java.util.Optional;

import diamond.cms.server.model.EmailConfig;

public class UserNotInitException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = -5595096305303663654L;

    private EmailConfig emailConfig;
    public UserNotInitException(Optional<EmailConfig> config) {
        emailConfig = config.orElse(null);
    }
    public EmailConfig getEmailConfig() {
        return emailConfig;
    }
    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }


}
