package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.PageBean;
import com.itheima.model.ResultInfo;
import com.itheima.model.Route;
import com.itheima.service.RouteService;
import com.itheima.util.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RouteServlet",urlPatterns = "/route")
public class RouteServlet extends BaseServlet {

   private RouteService service = (RouteService)BeanFactoryUtils.getBean("RouteService");
    ResultInfo info;
    public void queryByPageRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;

        try {
            //获取pageNumber
            int pageNumber = 1;
            try {
                 pageNumber = Integer.parseInt(request.getParameter("pageNumber"));

            }catch (Exception e) {
                pageNumber = 1;
            }
            //自定义pageSize
            int pageSize = 8;
            //调用service 完成分页查询
            PageBean<Route> pb = service.queryByPageRank(pageNumber, pageSize);
            info = new ResultInfo(true);
            info.setData(pb);

        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(true, "服务器忙请稍后再试");
        }
        ObjectMapper mapper = new ObjectMapper();
        String infoJson = mapper.writeValueAsString(info);
        //System.out.println(infoJson);
        response.getWriter().write(infoJson);
    }

    /**
     * 查询list_detail详情页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.定义ResuletInfo info
        ResultInfo info;
        try {
            //2.获取参数rid
            String rid = request.getParameter("rid");
            //调用service 处理业务逻辑
            Route route = service.findRouteById(rid);
            info  = new ResultInfo(true);
            info.setData(route);
        } catch (Exception e) {
            e.printStackTrace();
            info  = new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonList = mapper.writeValueAsString(info);
        //System.out.println(jsonList);
        response.getWriter().write(jsonList);

    }

    /**
     * 查询route_list的分页展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void queryPageByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //1.首先获取请求携带的参数cid
            String cid = request.getParameter("cid");
            //获取rname 的值
            String rname = request.getParameter("rname");
            //2.获取请求携带的pageNumber
            int pageNumber = 1;

            try {
                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            }catch (Exception e){
                e.printStackTrace();
                pageNumber = 1;
            }
            //3.自定义pageSize
            int pageSize = 3;
            //4.调用service 完成分页查询,返回给pageBean
            //分页查询的已知条件
        /*
        已知条件:pageSize   每页显示条数
        其实索引:当前页的页码
        必须条件:每页显示条数 pageSize
        起始索引:(当前页码-1)*每页显示条数
        PageBean:pageSize :每页显示条数
        pageNumber :当前页
        totalPage:总页数
        totalRecord:总条数
        data:当前页的信息
         */
            PageBean<Route> pb = service.queryPageByCid(rname,cid,pageNumber,pageSize);
            info = new ResultInfo(true);
            info.setData(pb);
            //5.封装数据返回json数据
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(info);
        //System.out.println(s);
        response.getWriter().print(s);

    }
}
    