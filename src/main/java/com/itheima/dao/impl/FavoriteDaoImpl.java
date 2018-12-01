package com.itheima.dao.impl;

import com.itheima.dao.FavoriteDao;
import com.itheima.model.Favorite;
import com.itheima.model.Route;
import com.itheima.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    @Override
    /**
     * 查看是否收藏过该路线
     */
    public Favorite isFavorite(int rid, int uid) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_favorite where rid = ? and uid = ? ";
            Favorite favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
            return favorite;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 添加中间表,完成收藏
     * @param favorite
     * @param template
     */
    @Override
    public void addFavorite(Favorite favorite, JdbcTemplate template) throws Exception{
        String sql = "insert into tab_favorite values(?,?,?) ";
        template.update(sql,favorite.getRid(),favorite.getDate(),favorite.getUid());
    }

    /**
     * 修改route的表的收藏次数
     * @param rid
     * @param template
     */
    @Override
    public void updateRouteCount(String rid, JdbcTemplate template) {
        String sql = "update tab_route set count = count + 1 where rid = ? ";
        template.update(sql,rid);
    }

    /**
     * 查询记录的总条数
     * @param uid
     * @return
     */
    @Override
    public int findMyFavoriteCount(int uid) throws  Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_favorite where uid = ? ";
        Integer i = jdbcTemplate.queryForObject(sql, int.class, uid);
        return i;
    }

    /**
     * 查询所有的该用户所有收藏的信息
     * @param startIndex
     * @param pageSize
     * @param uid
     * @return
     */
    @Override
    public List<Favorite> findMyFavoriteByPage(int startIndex, int pageSize, int uid) throws Exception{
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        // 查询当前用户的收藏线路有哪些 favorite
        String sql = "select * from tab_favorite where uid = ? limit ?,? ";
        List<Favorite> listFavorite = template.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid,startIndex,pageSize);
        // 查询线路详细信息  [1,2,3]
        for (Favorite favorite : listFavorite) {
            sql = "select * from tab_route where rid = ? ";
            Route route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), favorite.getRid());
            favorite.setRoute(route);
        }
        return listFavorite;
    }
}
