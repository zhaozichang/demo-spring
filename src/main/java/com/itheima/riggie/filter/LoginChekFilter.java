package com.itheima.riggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.riggie.common.BaseContext;
import com.itheima.riggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成了登录
 */
@WebFilter(filterName ="LoginChekFilter",urlPatterns = "/*")
@Slf4j
public class LoginChekFilter implements Filter {

    //路径匹配器，同时还支持通配符写法
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
//        1.获取本次请求的路径
        String requestURL = request.getRequestURI();

        log.info("拦截到请求"+ requestURL);

        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
//        2.判断路径是否需要被拦截
        boolean check = check(urls, requestURL);

//        3.如果不需要处理,则直接放行
        if (check) {
            filterChain.doFilter(request,response);
            log.info("本次请求不需要处理"+requestURL);
            return;
        }
//        4.如果需要处理,判断是否已经登录
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户以登录,用户id为"+request.getSession().getAttribute("employee"));

                    //若是已经登录，就把用户id存入线程
            Long empId =(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }
//        5.如果没有登录,则返回未登录结果,通过输出流的方式向我们的客户端响应数据,前端已经写了返回登陆页面
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /***
     * 路径匹配，检查当前路径是否放行
     * @return
     */
    public boolean check(String[] urls,String requestURL){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url,requestURL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
