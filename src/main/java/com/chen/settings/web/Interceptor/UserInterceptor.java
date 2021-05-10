package com.chen.settings.web.Interceptor;

import com.chen.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenhongchang
 * @date 2021/5/8 0008 - 下午 7:26
 */
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)request.getSession().getAttribute("user");
        System.out.println("执行UserInterceptor的pre拦截器");
        //System.out.println(user);
        if(null == user){
            // 请求转发到登录页
            //request.getRequestDispatcher("/index.jsp").forward(request,response);
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }
}
