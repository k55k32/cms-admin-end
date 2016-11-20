package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CCatalogPojo;

public class Catalog extends CCatalogPojo{

    /**
     *
     */
    private static final long serialVersionUID = 2847429716821576726L;

    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
