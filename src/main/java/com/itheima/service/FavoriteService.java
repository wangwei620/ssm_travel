package com.itheima.service;

import com.itheima.model.Favorite;
import com.itheima.model.PageBean;
import com.itheima.model.User;

public interface FavoriteService {

    boolean isFavorite(int rid, User user)throws Exception;

    void addFavorite(String rid, User user) throws Exception;

    PageBean<Favorite> findMyFavoriteByPage(String pageNumber, int uid, int pageSize) throws Exception;
}
