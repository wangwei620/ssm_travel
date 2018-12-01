package com.itheima.dao;

import com.itheima.model.Category;

import java.util.List;

public interface CategoryDao {
    /**
     * 查询导航条的所有数据
     * @return
     */
   List<Category> findAll() throws Exception;
}
