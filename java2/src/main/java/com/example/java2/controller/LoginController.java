//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.Java2.controller;

import com.example.Java2.service.IUserService;
import com.example.Java2.vo.LoginVo;
import com.example.Java2.vo.RegisterVo;
import com.example.Java2.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
public class LoginController {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    public LoginController() {
    }

    @GetMapping("/login")
    public String docym() {
    System.out.println(111);
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    RespBean dologin2(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("{}", loginVo);
        return userService.doLogin(loginVo, request, response);


    }
    @RequestMapping("/doRegister")
    @ResponseBody
    public RespBean doRegister(@Valid RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response) {

        return userService.doRegister(registerVo, request, response);
    }
}
