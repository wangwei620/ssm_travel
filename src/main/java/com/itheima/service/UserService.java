package com.itheima.service;

import com.itheima.model.User;

public interface UserService {
    /**
     * 处理注册的业务逻辑
     * @param user
     * @return
     */
    boolean register(User user) throws Exception;
    /**
     * 账号激活
     * @param code
     * @return
     */
    boolean active(String code);
    /**
     * 处理用户登录的请求
     *
     * @param user
     * @return
     */
    public User login(User user) throws Exception;
    /**
     * 查看用户名是否存在
     * @param username
     * @return
     */
    public boolean chcekName(String username);
}
