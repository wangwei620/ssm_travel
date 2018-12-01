package com.itheima.dao.impl;

import com.itheima.dao.CategoryDao;
import com.itheima.model.Category;
import com.itheima.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    /**
     * 查询导航条的所有数据
     * @return
     */
    public List<Category> findAll() throws Exception{
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_category ";
        List<Category> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
        return query;
    }

}
