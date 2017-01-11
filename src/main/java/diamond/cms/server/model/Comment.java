package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CCommentPojo;

public class Comment extends CCommentPojo{

    /**
     *
     */
    private static final long serialVersionUID = -7111751513291876853L;

    private String articleTitle;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }


}
