package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CTagPojo;

public class Tag extends CTagPojo{

    /**
     *
     */
    private static final long serialVersionUID = -2966642435854593321L;

    private Integer articleCount;

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }
}
