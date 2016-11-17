package diamond.cms.server.core;

import java.util.List;

public class Page<T> {
    private int pageCount;
    private int total;
    private int currentPage;
    private int pageSize;
    private List<T> data;

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return pageSize * currentPage;
    }
}
