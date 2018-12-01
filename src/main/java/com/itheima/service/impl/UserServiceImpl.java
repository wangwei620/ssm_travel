package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.model.User;
import com.itheima.service.UserService;
import com.itheima.util.BeanFactoryUtils;
import com.itheima.util.MD5Utils;
import com.itheima.util.MailUtils;
import com.itheima.util.UUIDUtils;

public class UserServiceImpl implements UserService {
     private UserDao userDao = (UserDao) BeanFactoryUtils.getBean("UserDao");
    /**
     * 处理注册的业务逻辑
     * @param user
     * @return
     */
    public boolean register(User user) throws Exception {

        //设置加密的密码
        String password = user.getPassword();
        String s = MD5Utils.encodeByMd5(password);
        user.setPassword(s);
        //设置用户默认状态
        user.setStatus("N");
        //设置激活码
        user.setCode(UUIDUtils.getUuid());
        String code = user.getCode();
        //调用dao完成用户的注册
       int count =  userDao.register(user);
        if (count == 1){//数据库插入成功返回1

       //发送到邮箱中
            String emailMsg = "<a href='localhost:8080/day14travel/user?action=active&code="+code+"'>点击激活黑马旅游帐号</a>";
            //发送激活邮件,根据用户提供的注册邮箱发送激活邮件
            MailUtils.sendMail(user.getEmail(), emailMsg);
            return true;
        }
        return false;
    }

    /**
     * 账号激活
     * @param code
     * @return
     */
    public boolean active(String code) {
        //根据表的唯一表示进行更改状态
        //1.查询用户信息
        User user  = userDao.findUserByCode(code);
        //判断是否是该用户
        if (user==null){
            return false;
        }
        //修改状态
        int count = userDao.active(user);
        //System.out.println(count);
        return count == 1?true:false;
    }

    /**
     * 处理用户登录的请求
     *
     * @param user
     * @return
     */
    public User login(User user) throws Exception {
        //通过用户输入的密码变为加密过的
        user.setPassword(MD5Utils.encodeByMd5(user.getPassword()));
        //调用dao完成查询
      return   userDao.login(user);
    }

    /**
     * 查看用户名是否存在
     * @param username
     * @return
     */
    public boolean chcekName(String username) {
        Integer i = userDao.checkName(username);
       return i > 0? true:false;
    }
}
