package com.itheima.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.model.ResultInfo;
import com.itheima.service.CategoryService;
import com.itheima.util.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "CategoryServlet",urlPatterns = "/category")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = (CategoryService) BeanFactoryUtils.getBean("CategoryService");

    /**
     * 通过category表查询所有的导航栏信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public  void queryAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultInfo info;
        try {

            //调用service 调用category信息

           String category =  service.findAll();
           // System.out.println(category);
            info = new ResultInfo(true);
            info.setData(category);
        }catch (Exception e){
            info = new ResultInfo(false);
            e.printStackTrace();
        }
        //将info的信息发送给前台
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(info);
        response.getWriter().print(s);
    }
}
    