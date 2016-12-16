package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CArticleTagPojo;

public class ArticleTag extends CArticleTagPojo{

    /**
     *
     */
    private static final long serialVersionUID = -1898991689036565234L;

    private Tag tag;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

}
