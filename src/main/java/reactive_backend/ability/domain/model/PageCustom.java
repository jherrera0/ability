package reactive_backend.ability.domain.model;

import java.util.List;

public class PageCustom<T>{
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private List<T> items;

    public PageCustom() {
    }

    public PageCustom(Integer currentPage, Integer pageSize, Integer totalPages, List<T> items) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.items = items;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
