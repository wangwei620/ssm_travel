package com.itheima.dao.impl;

import com.itheima.dao.UserDao;
import com.itheima.model.User;
import com.itheima.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    /**
     * 注册
     * @param user
     * @return
     */
    public int register(User user) {
        try {
            //获取连接池
            //System.out.println(user.getName());
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            //编写sql
            String sql = "insert into tab_user values(null,?,?,?,?,?,?,?,?,?) ";
            Object[] params = {
                    user.getUsername(),
                user.getPassword(),
                    user.getName(),
                    user.getBirthday(),
                    user.getSex(),
                    user.getTelephone(),
                    user.getEmail(),
                    user.getStatus(),
                    user.getCode(),
            };
            int i = jdbcTemplate.update(sql, params);
            //System.out.println(i);
            return i;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据code查询用户信息
     * @param code
     * @return
     */
    public User findUserByCode(String code) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_user where code = ? ";
            return  jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 激活
     * @param user
     * @return
     */
    public int active(User user) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "update tab_user set status = 'Y' where uid = ? ";
            int i = jdbcTemplate.update(sql, user.getUid());
            System.out.println(i);
            return i;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 查询数据登录
     * @param user
     * @return
     */

    @Override
    public User login(User user) {
        try {
            JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_user where username = ? and password = ? ";
            return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),user.getUsername(),user.getPassword());
        } catch (DataAccessException e) {
            return null;
        }
    }



    /**
     *
     * @param username
     * @return
     */
    public Integer checkName(String username) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select count(*) from tab_user where username = ? ";
            Integer i = jdbcTemplate.queryForObject(sql, int.class, username);
            return i;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
