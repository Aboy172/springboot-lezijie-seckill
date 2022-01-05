//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.controller;

import com.example.java2.service.IUserService;
import com.example.java2.vo.LoginVo;
import com.example.java2.vo.RegisterVo;
import com.example.java2.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/login")
    public String docym() {
        return "login";
    }

  @PostMapping("/doLogin")
  @ResponseBody
  RespBean dologin(
      @Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("{}", loginVo);
        return userService.doLogin(loginVo, request, response);


    }
    @PostMapping("/doRegister")
    @ResponseBody
    public RespBean doRegister(@Valid RegisterVo registerVo, HttpServletRequest request, HttpServletResponse response) {

        return userService.doRegister(registerVo, request, response);
    }

  //  @GetMapping("/captcha")
  //  public String verifyCode(HttpServletResponse response) {
  //    response.setContentType("image/gif");
  //    response.setHeader("Pragma", "No-cache");
  //    response.setHeader("Cache-Control", "no-cache");
  //    response.setDateHeader("Expires", 0);
  //    // 生成验证码
  //    ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 28, 3);
  //    try {
  //      boolean out = captcha.out(response.getOutputStream());
  //      if (out) {
  //        return "captcha";
  //      }
  //
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //    return null;
  //  }
}
