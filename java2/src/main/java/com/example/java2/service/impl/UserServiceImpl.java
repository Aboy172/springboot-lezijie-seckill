//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java2.exception.GlobalException;
import com.example.java2.mapper.UserMapper;
import com.example.java2.pojo.User;
import com.example.java2.service.IUserService;
import com.example.java2.utils.CookieUtil;
import com.example.java2.utils.MD5Util;
import com.example.java2.utils.UUIDUtil;
import com.example.java2.utils.ValidatorUtil;
import com.example.java2.vo.LoginVo;
import com.example.java2.vo.RegisterVo;
import com.example.java2.vo.RespBean;
import com.example.java2.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    private static final String salt = "1a2b3c4d";

    @Autowired
    private RedisTemplate redisTemplate;

  @Override
  public RespBean doLogin(
      LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = (User) userMapper.selectById(mobile);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if (!MD5Util.fromPassToDBPass(password,user.getSlat()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user);
//        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }


    @Override
    public User getUserByCookie (String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:"+userTicket);
        if (user != null) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
    @Override
    public RespBean doRegister (RegisterVo registerVo,HttpServletRequest request,HttpServletResponse response) {
        Long mobile = registerVo.getMobile();

        String password = registerVo.getPassword();
        String username = registerVo.getUsername();

        if (StringUtils.isEmpty(String.valueOf(mobile)) || (StringUtils.isEmpty(password)) || StringUtils.isEmpty(username))
            return RespBean.error(RespBeanEnum.INFO_ERROR);

        User user = userMapper.selectById(mobile);
        if (user != null) {
            return RespBean.error(RespBeanEnum.USER_ERROR);
        }
        if (!ValidatorUtil.isMobile(mobile.toString())) {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }
        User ruser = new User();
        ruser.setId(mobile);
        ruser.setNickname(username);
        ruser.setPassword(MD5Util.fromPassToDBPass(password,salt));
        ruser.setSlat(salt);
        ruser.setRegisterDate(new Date());
        ruser.setLastLoginDate(new Date());
        ruser.setLoginCount(1);
        userMapper.insert(ruser);
        return RespBean.success();
    }

    /**
     *更新密码功能
     * @param userTicket
     * @param password
     * @param request
     * @param response
     * @return
     */
    @Override
    public RespBean updatepassword (String userTicket,String password,HttpServletRequest request,
                                    HttpServletResponse response) {
        User user = getUserByCookie(userTicket,request,response);
        if (user == null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_ERROR);
        }
        user.setPassword(MD5Util.inputPassToDBPss(password,user.getSlat()));
        int result = userMapper.updateById(user);
        //删除Redis
        if (result ==1){
            redisTemplate.delete("user"+userTicket);
            return RespBean.success();

        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_ERROR);

    }





}