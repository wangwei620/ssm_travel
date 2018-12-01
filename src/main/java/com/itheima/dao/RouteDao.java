package com.itheima.dao;

import com.itheima.model.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 查询总条数
     *
     * @param rname
     * @param cid
     * @return
     * @throws Exception
     */
     int findCount(String cid,String rname) throws Exception;
    /**
     * 查询分页的所有的信息
     * @param cid
     * @param startIndex
     * @param pageSize
     * @param rname
     * @return
     */
    public List<Route> queryPageByCid(String cid, int startIndex, int pageSize, String rname) throws Exception;

    /**
     * 查询list_datail详情页面
     * @param rid
     * @return
     */
    Route findRouteById(String rid) throws Exception;

    int findAllCount() throws Exception;

    List<Route> queryByPageRank(int startIndex, int pageSize) throws Exception;
}
