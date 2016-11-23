package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CArticlePojo;

//@JsonInclude(Include.NON_EMPTY)
public class Article extends CArticlePojo{
    /**
     *
     */
    private static final long serialVersionUID = 4376999719216618768L;

    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
