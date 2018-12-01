package com.itheima.service;

import com.itheima.model.PageBean;
import com.itheima.model.Route;

public interface RouteService {
    /**
     * 分页路线的查找
     * @param cid
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageBean queryPageByCid(String rname, String cid, int pageNumber, int pageSize) throws Exception;

    /**
     * 查询list_datail详情页面
     * @param rid
     * @return
     */
    Route findRouteById(String rid) throws Exception;

    PageBean<Route> queryByPageRank(int pageNumber, int pageSize) throws Exception;
}
