package com.chen.vo;

import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 下午 6:46
 */
public class PageNationVo<T> {

    private int total;
    private List<T> dataList;

    public PageNationVo(int total, List<T> dataList) {
        this.total = total;
        this.dataList = dataList;
    }

    public PageNationVo() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
