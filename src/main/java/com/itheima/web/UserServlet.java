package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.ResultInfo;
import com.itheima.model.User;
import com.itheima.service.UserService;
import com.itheima.util.BeanFactoryUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet {
   //使用工厂的方式创建userService对象
  private   UserService userService = (UserService)BeanFactoryUtils.getBean("UserService");
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 处理post请求乱码问题
        //获取请求标识
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            register(request, response);
        }else if ("active".equals(action)){
            active(request,response);
        }else if ("login".equals(action)){
            login(request,response);
        }else if ("getUserInfo".equals(action)){
            getUserInfo(request,response);
        }else if ("logout".equals(action)){
            logout(request,response);
        }
    }*/

    /**
     * 登出案例
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        //直接销毁session域
        request.getSession().invalidate();
       response.sendRedirect(request.getContextPath());
    }
    public void checkName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得前台传来的name数据
        String username = request.getParameter("username");
        //System.out.println(username);
        //我们能拿到这个用户输入的数据
           ResultInfo info = new ResultInfo();
       boolean flag =  userService.chcekName(username);
       if (flag){
           info.setFlag(flag);
       }else{
           info.setFlag(flag);
       }//返回非前台页面
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(info);
        response.getWriter().print(s);
    }

    /**
     * 获取当前用户的登陆信息
     * @param
     * @param
     */
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
        ResultInfo info;
        HttpSession session = request.getSession();
        User  user = (User)session.getAttribute("user");
        if (user==null){
            info = new ResultInfo(false);
        }else {
            info  = new ResultInfo(true);
            info.setData(user);
        }
        //返回给前台
        ObjectMapper mappper = new ObjectMapper();
        String s = mappper.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //定义返回结果信息
        ResultInfo info;
        ObjectMapper om = new ObjectMapper();
        try {

            //首先获得序列化的参数
            //1.校验验证码
            //a.获取请求携带的验证码
            String ucode = request.getParameter("ucode");
            //System.out.println(ucode);
            //b.获取session中的验证码
            HttpSession session = request.getSession();
            String  scode = (String)session.getAttribute("scode");
            //获得完立即销毁
            session.removeAttribute("scode");
            //c.校验
            if (ucode==""){
                //验证码为空
               info =  new ResultInfo(false,"验证码不能为空");
               //把这个信息写会到前台
                String s = om.writeValueAsString(info);
                response.getWriter().write(s);
                return;
            }
            if (scode!=null && !scode.equalsIgnoreCase(ucode)){
                //验证码错误
                info = new ResultInfo(false,"验证码错误");
                String s = om.writeValueAsString(info);
                response.getWriter().write(s);
                return;
            }
            //2.执行登录
            //a.获取请求携带的用户名和密码
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            //封装实体
            BeanUtils.populate(user,map);
            //打印封装的实体
            //System.out.println(user);
            //b.调用service完成登录的业务逻辑
            User u =  userService.login(user);
            //c.判断查询结果
            if (u==null){
                //用户名或者密码错误
                info = new  ResultInfo(false,"用户名或者密码错误");
            }else {
                //判断用户是否激活
                if ("N".equals(u.getStatus())){
                    info = new ResultInfo(false,"您还未激活,请激活后在登录...");
                }else{
                    //登录成功
                    info = new ResultInfo(true);
                    //因为我们要进行头部的的动态显示,所以我们要进行设置session域,共享登录用户登录的信息
                    session.setAttribute("user",u);
                }
            }
            String s = om.writeValueAsString(info);
            response.getWriter().write(s);
        } catch (Exception e) {
            e.printStackTrace();
            info = new  ResultInfo(false,"当前功能正在维护...");
            String s = om.writeValueAsString(info);
            response.getWriter().write(s);
        }
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        //System.out.println(code);
        boolean flag =  userService.active(code);
        if (flag){
            //激活成功
            //重定向到登陆向到进行登陆页面
            response.sendRedirect(request.getContextPath()+"/login.html");
            //request.getRequestDispatcher("/login.html").forward(request,response);
        }else {
            //未激活
            //request.getRequestDispatcher("/error/500.html").forward(request,response);
            response.sendRedirect(request.getContextPath()+"/error/500.html");
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo info = null;
        try {
            //获取携带数据
            Map<String, String[]> map = request.getParameterMap();
            //封装数据
            User user = new User();
            BeanUtils.populate(user, map);
            //用户的验证码
            String ucode = request.getParameter("check");
            //获取session中的验证码
            HttpSession session = request.getSession();
            String scode = (String)session.getAttribute("scode");
            //首先进行验证的判断
            if ("".equals(ucode)){
                info = new ResultInfo(false,"验证码不能为空");
            }else{
                if (!ucode.equalsIgnoreCase(scode)){
                    info = new ResultInfo(false,"验证码错误");
                }else {
                    info = new ResultInfo(true);
                    session.invalidate();
                    //调用service处理业务逻辑
                    //System.out.println(user.getName());
                    boolean flag = userService.register(user);
                    //System.out.println(flag);
                    //判断
                    if (flag) {
                        //成功
                        info = new ResultInfo(true);
                    } else {
                        //失败
                        info = new ResultInfo(false, "当前功能正在维修");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //失败
            info = new ResultInfo(false, "当前功能正在维修");
        }
        //转换为json字符串,返回给浏览器
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(info);
            //System.out.println(s);
            response.getWriter().write(s);//发送信息
    }

    /*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8"); // 处理响应乱码问题:字节流需getBytes("UTF-8")
        //response.getWriter().write("你好 servlet!");
        this.doPost(request,response);
    }*/
}
    