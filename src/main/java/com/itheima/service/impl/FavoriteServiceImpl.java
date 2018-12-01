package com.itheima.service.impl;

import com.itheima.dao.FavoriteDao;
import com.itheima.model.Favorite;
import com.itheima.model.PageBean;
import com.itheima.model.User;
import com.itheima.service.FavoriteService;
import com.itheima.util.BeanFactoryUtils;
import com.itheima.util.JDBCUtils;
import com.itheima.util.PageUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = (FavoriteDao) BeanFactoryUtils.getBean("FavoriteDao");

    /**
     * 判断该路线是否收藏过
     *
     * @param rid
     * @param user
     * @return
     */
    @Override
    public boolean isFavorite(int rid, User user) {

        try {
            Favorite favorite = favoriteDao.isFavorite(rid, user.getUid());
            return favorite == null ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加个人收藏
     *
     * @param rid
     * @param user
     */
    @Override
    public void addFavorite(String rid, User user) throws Exception {
        //调用FavoriteDao处理添加的处理
        Favorite favorite = new Favorite();
        favorite.setRid(Integer.parseInt(rid));
        favorite.setUid(user.getUid());
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        favorite.setDate(format.format(date));
        // 事务控制
        //一: 创建JdbcTemplate对象
        //1.获取连接池对象
        DataSource dataSource = JDBCUtils.getDataSource();
        //2.创建jdbcTemplate实例 // template从连接池中获取一个连接
        JdbcTemplate template = new JdbcTemplate(dataSource);
        //二: 启动事务管理器 手动提交事务
        //3.启动事务管理器(将conn和当前线程做绑定)
        TransactionSynchronizationManager.initSynchronization();
        //4.获取连接 : 获取JdbcTemplate所使用的连接对象
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            //5.将连接的事务,设置为手动事务提交
            conn.setAutoCommit(false);
            //===== 业务处理
            //1.添加中间表数据信息
            favoriteDao.addFavorite(favorite, template);
            //我们同事要修改route表的收藏次数
            //2.修改收藏次数
            favoriteDao.updateRouteCount(rid, template);
            // 提交事务
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 事务回顾
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            // 将异常抛给调用者,告知执行失败
            throw e;
        } finally {
            // 将conn对象和当前线程解除绑定
            TransactionSynchronizationManager.clearSynchronization();
            // 修改为自动事务提交
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看我的收藏
     * @param pageNumber
     * @param uid
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Favorite> findMyFavoriteByPage(String pageNumber, int uid, int pageSize) throws Exception{

        //1.查询分页数据总条数  根据用户uid查询收藏的线路总条数
        int totalRecord = favoriteDao.findMyFavoriteCount(uid);
        //2.创建PageBean<totalCount,pageSize,pageNumber>
        PageBean<Favorite> pb = new PageBean<Favorite>(Integer.parseInt(pageNumber), pageSize, totalRecord);
        //3.查询当前页面的数据信息
        //计算起始索引
        int startIndex = pb.getStartIndex();
        //调用dao查询  List<实体>
        List<Favorite> list = favoriteDao.findMyFavoriteByPage(startIndex,pageSize,uid);
        //封装到pb中
        pb.setData(list);
        //4.计算前五后四.
        int[] ints = PageUtils.pagination(Integer.parseInt(pageNumber), pb.getTotalPage());
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);

        return pb;
    }
}
