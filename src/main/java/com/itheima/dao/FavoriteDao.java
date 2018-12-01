package com.itheima.dao;

import com.itheima.model.Favorite;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface FavoriteDao {

    Favorite isFavorite(int rid, int uid)throws Exception;

    void addFavorite(Favorite favorite, JdbcTemplate template) throws Exception;

    void updateRouteCount(String rid, JdbcTemplate template);

    int findMyFavoriteCount(int uid) throws Exception;

    List<Favorite> findMyFavoriteByPage(int startIndex, int pageSize, int uid) throws Exception;
}
