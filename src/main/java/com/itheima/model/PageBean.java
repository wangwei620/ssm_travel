package com.itheima.model;

import java.util.List;

public class PageBean<T> {
    /**
     * 2个传
     * pageNumber 页码
     * pageSize 每页显示个数
     * 2个算
     * startIndex 开始索引
     * totalPage 总页数
     * 2个查
     * data 查询的分页数据
     * totalRcord   记录数
     * 2个参数 扩展
     * start 开始
     * end 结束
     */
    private int pageNumber;
    private int pageSize;
    private int startIndex;
    private int totalPage;
    private int totalRecord;
    private List<T> data;

    private int start ;
    private int end ;

    /**
     * 计算 循环的开始 和 结束
     */
    private void jisuan() {
        /*start = 10;
        end=20;*/
        if(getTotalPage() < 10){ //1. 总页数不满足于10 没有必要前四后五   totalPage需要第一次调用get方法 初始化计算
            start=  1;
            end = totalPage;
        }else{ //2. 总页数大于10
            start = pageNumber - 4;
            end = pageNumber + 5;

            //还需要判断极限值
            //最小
            if(start < 1 ){
                start=1; // pageNumber  1 2 3 4  5
                end=10;
            }
            //最大
            if(end > totalPage ){
                end = totalPage;
                start = totalPage-9;
            }


        }




    }

    public int getStart() {
        jisuan();
        return start;
    }
    public int getEnd() {
        jisuan();
        return end;
    }




    /**
     * 计算总页数 调用get 返回总页数
     * @return
     */
    public int getTotalPage() {
        if(totalRecord % pageSize == 0 ){
            totalPage = totalRecord / pageSize;
        }else{
            totalPage = totalRecord / pageSize+1;
        }
        return totalPage;
    }
    /**
     * 调用getStartIndex 直接计算
     * @return
     */
    public int getStartIndex() {
        startIndex =  (pageNumber-1) * pageSize;
        return startIndex;
    }
    /**
     * 每页显示个数 和 页码
     * 没有无参构造(防止有人忘了给两个参数)
     * @param pageNumber
     * @param pageSize
     */
    public PageBean(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public PageBean(int pageNumber, int pageSize, int totalRecord) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
    }

    public void setStart(int start) {
        this.start = start;
    }


    public void setEnd(int end) {
        this.end = end;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }



    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }



    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
