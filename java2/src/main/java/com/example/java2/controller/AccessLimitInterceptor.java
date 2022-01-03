package com.example.java2.controller;

import com.alibaba.fastjson.JSON;
import com.example.java2.config.AccessLimit;
import com.example.java2.config.UserContext;
import com.example.java2.pojo.User;
import com.example.java2.service.IUserService;
import com.example.java2.utils.CookieUtil;
import com.example.java2.vo.RespBean;
import com.example.java2.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/** @author cym 2022/1/4 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
  @Autowired private IUserService userService;
  @Autowired private RedisTemplate redisTemplate;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {
      User user = getUser(request, response);
      UserContext.setUser(user);
      HandlerMethod hm = (HandlerMethod) handler;
      AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
      if (accessLimit == null) {
        return true;
      }
      int second = accessLimit.second();
      int maxCount = accessLimit.maxCount();
      boolean needLogin = accessLimit.needLogin();
      String key = request.getRequestURI();
      if (needLogin) {
        if (user == null) {
          redner(response, RespBeanEnum.SEESION_ERROR);
          return false;
        }
        key += ":" + user.getId();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer count = (Integer) valueOperations.get(key);
        if (count == null) {
          valueOperations.set(key, 1, second, TimeUnit.SECONDS);
        } else if (count < maxCount) {
          valueOperations.increment(key);
        } else {
          redner(response, RespBeanEnum.REQUEST_MANY_ERROR);
        }
      }
      return true;
    }

    User user = getUser(request, response);
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  /**
   * 返回对象的处理
   *
   * @param response
   * @param respBeanEnum
   */
  private void redner(HttpServletResponse response, RespBeanEnum respBeanEnum) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    RespBean respBean = RespBean.error(respBeanEnum);
    out.write(JSON.toJSONString(respBean));
    out.flush();
    out.close();
  }

  /**
   * 获取当前用户
   *
   * @param request
   * @param response
   * @return
   */
  private User getUser(HttpServletRequest request, HttpServletResponse response) {
    String ticket = CookieUtil.getCookieValue(request, "userTicket");
    if (StringUtils.isEmpty(ticket)) {
      return null;
    }
    return userService.getUserByCookie(ticket, request, response);
  }
}
