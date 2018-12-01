package com.itheima.util;

import java.util.Arrays;

public class PageUtils {
    /**
     * 10页-分页条：前5后4动态分页，计算起始页
     *
     * @param pageNumber 当前页码
     * @param totalPage  总共有多少页
     * @return int[0]：开始页码； int[1]：结束页面
     */
    public static int[] pagination(int pageNumber, int totalPage) {
        //初始化开始页为1
        int start = 1;
        //初始化结束页为总页数
        int end = totalPage;

        //如果总页数超过10页，需要计算开始页和结束页
        if (totalPage > 10) {
            //计算开始页
            start = (pageNumber <= 5) ? 1 : (pageNumber - 5);
            //计算结束页：开始页面之后，再显示9个页码，共10个页码
            end = start + 9;
            //处理结束页越界的情况
            if (end > totalPage) {
                end = totalPage;
                start = end - 9;
            }
        }
        return new int[]{start, end};
    }

    public static void main(String[] args) {
        int[] pagination = pagination(7, 11);
        System.out.println(Arrays.toString(pagination));
    }
}
