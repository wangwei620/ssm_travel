package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.*;
import com.itheima.service.FavoriteService;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "FavoriteServlet",urlPatterns = "/favorite")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService favoriteService = (FavoriteService)BeanFactoryUtils.getBean("FavoriteService");

    /**
     * 查找我收藏的列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findMyFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            String pageNumber = request.getParameter("pageNumber");
            //2.获取用户的uid
            User user = (User)request.getSession().getAttribute("user");
            int uid = user.getUid();
            //3.自定义每页显示的条数
            int pageSize = 4;
            //4.调用service处理业务逻辑
            PageBean<Favorite> pb =  favoriteService.findMyFavoriteByPage(pageNumber,uid,pageSize);
            info = new ResultInfo(true);
            info.setData(pb);

        } catch (Exception e) {
            e.printStackTrace();
            info  = new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        //System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0.定义ResultInfo
        ResultInfo info;
        try {
            //1.首先获得当前路线的rid
            ObjectMapper mapper = new ObjectMapper();
            String rid = request.getParameter("rid");
            //2.获得session中的用户
            User user = (User)request.getSession().getAttribute("user");
            //3.判断用户是否登陆
            if (user==null){
                //返回不能登陆的信息
                info = new ResultInfo(false);
                info.setData(-1);
                String infoJson = mapper.writeValueAsString(info);
                response.getWriter().write(infoJson);
                return;
            }else{
                //反之,我们添加收藏调用service处理业务逻辑
                FavoriteService favoriteService = (FavoriteService)BeanFactoryUtils.getBean("FavoriteService");
                favoriteService.addFavorite(rid,user);
                //生成信息,目的是查看添加的次数,更新到前台页面
                RouteService routeService = (RouteService)BeanFactoryUtils.getBean("RouteService");
                Route route = routeService.findRouteById(rid);
                info = new ResultInfo(true);
                info.setData(route.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            info  = new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper mapper = new ObjectMapper();
        String infoJson = mapper.writeValueAsString(info);
        response.getWriter().write(infoJson);
    }
    /**
     * 查看收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;//定义返回数据
        try {
            //1.判断用户是否登陆
            User user = (User)request.getSession().getAttribute("user");

            //2.获取rid
            String rid = request.getParameter("rid");
            //判断
        /*
        -1:表示未登陆
        1:表示未收藏
        true:表示收藏
         */
            if (user==null){
                //未登陆
                info = new ResultInfo(false);
                info.setData(-1);
            }else{
                //用户已经登陆
                //判断该路线是否收藏过
                //调用service 处理业务逻辑
                int ri = Integer.parseInt(rid);
                boolean flag = favoriteService.isFavorite(ri,user);
                //System.out.println(flag);
                //判断该用户是否收藏
                if (flag){
                    //用户已经收藏
                    info = new ResultInfo(true);
                }else {
                    //用户未收藏
                    info = new ResultInfo(false);
                    info.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");
        }
        //json格式返回
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(info);
        //System.out.println(jsonList);
        response.getWriter().print(jsonList);
    }
}
    