package diamond.cms.server.model;

import diamond.cms.server.model.jooq.tables.pojos.CArticlePojo;

public class Article extends CArticlePojo{
    /**
     *
     */
    private static final long serialVersionUID = 4376999719216618768L;

    /**
     * 删除的文章
     */
    public static final int STATUS_DELETE = 0;
    /**
     * 未发布（草稿）
     */
    public static final int STATUS_UNPUBLISH = 1;
    /**
     * 已发布（发布）
     */
    public static final int STATUS_PUBLISH = 2;

    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
