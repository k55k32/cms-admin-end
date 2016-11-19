package diamond.cms.server.core;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {
    private int pageCount = 1;
    private int total = 1;
    private int currentPage = 1;
    private int pageSize = 10;
    private List<T> data = new ArrayList<>();

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

    public static <E>PageResult<E> create(int page, int count, List<E> data) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setCurrentPage(page);
        pageResult.setTotal(count);
        pageResult.setData(data);
        return pageResult;
    }
}
