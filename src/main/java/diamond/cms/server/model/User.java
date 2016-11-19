package diamond.cms.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import diamond.cms.server.model.jooq.tables.pojos.CUserPojo;

@JsonIgnoreProperties({"password", "token", "expired"})
public class User extends CUserPojo{

    /**
     *
     */
    private static final long serialVersionUID = 7379195477421980824L;

}
