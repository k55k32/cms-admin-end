package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CCatalogPojo;

public class Catalog extends CCatalogPojo{

    /**
     *
     */
    private static final long serialVersionUID = 2847429716821576726L;

    private Integer articleCount;

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

}
