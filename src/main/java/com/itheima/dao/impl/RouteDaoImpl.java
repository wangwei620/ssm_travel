package com.itheima.dao.impl;

import com.itheima.dao.RouteDao;
import com.itheima.model.Category;
import com.itheima.model.Route;
import com.itheima.model.RouteImg;
import com.itheima.model.Seller;
import com.itheima.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    /**
     * 查询总条数
     *
     * @param rname
     * @param cid
     * @return
     * @throws Exception
     */
    public  int findCount(String cid,String rname) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_route where rflag = 1 ";
        //动态获取参数
        List list =     new ArrayList();
        if (cid !=null && !"".equals(cid)){
            sql += "and cid = ? ";
            list.add(cid);
        }
        if (rname!=null &&!"".equals(rname)){
            sql += " and rname like ? ";
            list.add("%"+rname+"%");
        }
//        String sql = "select count(*) from tab_route WHERE cid = ? and rname like ? ";
        //转化为list 为数组
        Object[] params = list.toArray();
        Integer i = jdbcTemplate.queryForObject(sql, int.class,params);
        //System.out.println(i);
        return i;
    }

    /**
     * 查询分页的所有的信息
     * @param cid
     * @param startIndex
     * @param pageSize
     * @param rname
     * @return
     */
    public List<Route> queryPageByCid(String cid, int startIndex, int pageSize, String rname) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

        String sql = "select * from tab_route where rflag = 1 ";
        //动态获取参数
        List list =   new ArrayList();
        if (cid !=null && !"".equals(cid)){
            sql += "and cid = ? ";
            list.add(cid);
        }
        if (rname!=null &&!"".equals(rname)){
            sql += " and rname like ? ";
            list.add("%"+rname+"%");
        }

        sql += "limit ?,? ";
        list.add(startIndex);
        list.add(pageSize);
        Object[] params = list.toArray();
        //String sql = "select * from tab_route where cid = ? and rname like ?  limit ?,? ";
        List<Route> query = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Route>(Route.class), params);
        return query;
    }

    @Override
    /**
     * 查询list_detail详情页面的数据,并封装到数中
     */
    public Route findRouteById(String rid) throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        //查询线路的所有的信息
        String sql = "select * from tab_route where rid = ? ";
        Route route = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        //2.查询当前路线的所属分类
        sql = "select * from tab_category where cid = ? ";
        Category category = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Category>(Category.class), route.getCid());
        //3.查询商家的信息
        sql = "select * from tab_seller where sid = ? ";
        Seller seller = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), route.getSid());
        //4.查询当前路线做包含的所有的信息
        sql = "select * from tab_route_img where rid = ? ";
        List<RouteImg> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        //封装所有的数据到Route线路中
        route.setCategory(category);
        route.setSeller(seller);
        route.setRouteImgList(query);
        //返回数据
        return route;
    }

    /**
     * 查询排行榜的总条数据
     * @return
     */
    @Override
    public int findAllCount() throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_route where rflag = 1 ";
        Integer i = jdbcTemplate.queryForObject(sql, int.class);
        return i;
    }

    /**
     * 排行榜的数据
     * @param startIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> queryByPageRank(int startIndex, int pageSize)throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_route where rflag = 1 order by count desc limit ?,? ";
        List<Route> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), startIndex, pageSize);
        return query;
    }
}
