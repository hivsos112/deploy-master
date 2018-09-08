package com.bsoft.deploy.http.interceptor;

import com.alibaba.fastjson.JSON;
import com.bsoft.deploy.context.Global;
import com.bsoft.deploy.http.HttpResult;
import com.bsoft.deploy.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * desc
 * Created on 2018/9/7.
 *
 * @author yangl
 */
public class AuthInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println(request.getHeader("Origin"));
        String ticket = request.getHeader("X-Token");
        String path = request.getServletPath();
        System.out.println("path:" + path + ",X-Token:" + ticket);
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 验证票据合法性
        if (StringUtils.isEmpty(ticket) || Global.getTokenStore().get(ticket) == null) {
            HttpResult result = new HttpResult(50014, "权限验证失败!");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print(JSON.toJSONString(result));
            logger.warn("没有权限访问地址" + path);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
