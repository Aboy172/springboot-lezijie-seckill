package com.example.java2.interceptor;

import com.example.java2.pojo.User;
import com.example.java2.service.IUserService;
import com.example.java2.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author cym    2022/1/2
 */
@Component
public class LoginInterceptor implements HandlerInterceptor  {

    @Autowired
    private IUserService userService;
/**
*
* @param request
* @param response
* @return
*/



    @Override
    public boolean preHandle (HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {
        String userTicket = CookieUtil.getCookieValue(request,"userTicket");
        User user = userService.getUserByCookie(userTicket,request,response);
        if (user ==null){
            response.sendRedirect( "/login");
            return false;
        }
        return true;
    }

    /**
     * 请求访问controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle (HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request,response,handler,modelAndView);
    }

    /**
     * 请求访问controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion (HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request,response,handler,ex);
    }
}
