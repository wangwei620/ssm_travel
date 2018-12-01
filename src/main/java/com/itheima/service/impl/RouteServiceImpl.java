package com.itheima.service.impl;

import com.itheima.dao.RouteDao;
import com.itheima.model.PageBean;
import com.itheima.model.Route;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactoryUtils;
import com.itheima.util.PageUtils;

import java.util.List;

public class RouteServiceImpl implements RouteService {
      private RouteDao routeDao = (RouteDao) BeanFactoryUtils.getBean("RouteDao");
    /**
     * 分页路线的查找
     * @param cid
     * @param pageNumber
     * @param pageSize
     * @return
     */

    public PageBean queryPageByCid(String rname, String cid, int pageNumber, int pageSize) throws Exception {
        //1.查询总条数据
        int totalRecord = routeDao.findCount(cid,rname);
        //System.out.println(totalRecord);
        //2.创建PageBean的对象
        PageBean<Route> pb = new PageBean<Route>(pageNumber, pageSize, totalRecord);
        //3.查询当前页的数据信息
        //a.获取起始索引
        //System.out.println(pb.getTotalRecord());
        int startIndex = pb.getStartIndex();
        //b.查询
        //将数据封装到pb中
        List<Route> list = routeDao.queryPageByCid(cid,startIndex,pageSize,rname);
        pb.setData(list);


        //前5后4
        int[] ints = PageUtils.pagination(pageNumber, pb.getTotalPage());
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);
        //4.返回pb
        return pb;
    }

    @Override
    /**
     * 查询list_detail详情页面
     */
    public Route findRouteById(String rid) throws Exception {

        //调用dao查询数据
      return   routeDao.findRouteById(rid);
    }

    /**
     * 收藏排行榜查询
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> queryByPageRank(int pageNumber, int pageSize) throws Exception {
       //查询总条数
        int totalRecord = routeDao.findAllCount();
        //创建pageBean对象
        PageBean<Route> pb = new PageBean<Route>(pageNumber,pageSize,totalRecord);
        //查询当前页的数据信息
        //起始索引
        int startIndex = pb.getStartIndex();
        int totalPage = pb.getTotalPage();
        //调用dao处理完成数据查询
        List<Route> listPb =  routeDao.queryByPageRank(startIndex,pageSize);
        //将查询结果封装到pb中
        pb.setData(listPb);
        //前5后4
        int[] ints = PageUtils.pagination(startIndex, totalPage);
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);
        //返回pb
        return pb;
    }
}
