package com.example.java2.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java2.pojo.User;
import com.example.java2.vo.LoginVo;
import com.example.java2.vo.RegisterVo;
import com.example.java2.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cym
 * @since 2021-12-12
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);


    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    RespBean doRegister(RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response);

    /**5
     * 更新用户的密码
     * @param userTicket
     * @param password
     * @return
     */
    RespBean updatepassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response);

    IPage<User> selectUserPage (Page<User> page);
}
