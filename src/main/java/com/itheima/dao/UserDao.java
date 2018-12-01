package com.itheima.dao;

import com.itheima.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    /**
     * 注册
     * @param user
     * @return
     */
    @Insert("insert into tab_user(username,password,name,birthday,sex,telephone,email,status," +
            "code) values(#{username},#{password},#{name},#{birthday},#{sex},#{telephone}," +
            "#{email},#{status},#{code})")
    int register(User user);
    /**
     * 根据code查询用户信息
     * @param code
     * @return
     */
    @Select("select * from tab_user where code = #{code}")
    User findUserByCode(String code);
    /**
     * 激活
     * @param user
     * @return
     */
    @Update("update tab_user set status = 'Y' where uid = #{uid}")
    public int active(User user);
    /**
     * 查询数据登录
     * @param user
     * @return
     */
    @Select("select * from tab_user where username = #{username} and password = #{password}")
      User login(User user);
    /**
     *
     * @param username
     * @return
     */
    @Select("select count(*) from tab_user where username = #{username}")
    public Integer checkName(String username);
}
