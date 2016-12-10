package diamond.cms.server.model;

public class ArticleDetail extends Article{

    /**
     *
     */
    private static final long serialVersionUID = -3503660465713964392L;

    private String nextId;
    private String nextTitle;
    private String beforeId;
    private String beforeTitle;

    public String getNextId() {
        return nextId;
    }
    public void setNextId(String nextId) {
        this.nextId = nextId;
    }
    public String getNextTitle() {
        return nextTitle;
    }
    public void setNextTitle(String nextTitle) {
        this.nextTitle = nextTitle;
    }
    public String getBeforeId() {
        return beforeId;
    }
    public void setBeforeId(String beforeId) {
        this.beforeId = beforeId;
    }
    public String getBeforeTitle() {
        return beforeTitle;
    }
    public void setBeforeTitle(String beforeTitle) {
        this.beforeTitle = beforeTitle;
    }

}
