package com.itheima.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //解决响应内容乱码
        response.setContentType("text/html;charset=utf-8");

        //放行之前，判断请求方式。如果是post提交，就设置请求参数的字符集为utf-8
        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method)) {
            request.setCharacterEncoding("utf-8");
            chain.doFilter(request, response);
        }else{
            chain.doFilter(req, response);
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
