package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CPageViewPojo;

public class PageView extends CPageViewPojo {

    /**
     *
     */
    private static final long serialVersionUID = 838690183976549349L;

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
