package com.tinshine.tmall.util;

public class Page {
    private int start; // the first item number of this page
    private int count; // item numbers per page
    private int total; // total item numbers
    private String param; // unknown parameters

    public Page() {
        count = defaultCount;
    }

    public Page(int start, int count) {
        this();
        this.start = start;
        this.count = count;
    }

    private static final int defaultCount = 5; // default 'count' value

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    // calculate total page number
    public int getTotalPage() {
        int totalPage = 1 + (getTotal() - 1) / getCount();
        return totalPage == 0 ? 1 : totalPage;
    }
    // calculate the number of the first item in the last page, from 0
    public int getLast() {
        int total = getTotal() - 1;
        return total - total % getCount();
    }

    public boolean isHavingPre() {
        return getStart() != 0;
    }

    public boolean isHavingNext() {
        return getStart() != getLast();
    }

    @Override
    public String toString() {
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHavingPre() + ", isHasNext()="
                + isHavingNext() + ", getTotalPage()=" + getTotalPage() + ", getLastPage()=" + getLast() + "]";
    }

}
