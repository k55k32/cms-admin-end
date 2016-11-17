package diamond.cms.server.entitys;

import diamond.cms.server.model.User;

public class UserEntiry extends User{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
