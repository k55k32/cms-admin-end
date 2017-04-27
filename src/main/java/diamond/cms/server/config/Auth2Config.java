package diamond.cms.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="auth2")
public class Auth2Config {
    private String githubClientId;
    private String githubClientSecret;
    
    public String getGithubClientId() {
        return githubClientId;
    }
    public void setGithubClientId(String githubClientId) {
        this.githubClientId = githubClientId;
    }
    public String getGithubClientSecret() {
        return githubClientSecret;
    }
    public void setGithubClientSecret(String githubClientSecret) {
        this.githubClientSecret = githubClientSecret;
    }
}
