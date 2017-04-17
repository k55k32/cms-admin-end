package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CGuestPojo;

public class Guest extends CGuestPojo{

    /**
     * 
     */
    private static final long serialVersionUID = 7370665916423774237L;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
