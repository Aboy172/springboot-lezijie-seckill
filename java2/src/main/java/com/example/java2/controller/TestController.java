package com.example.java2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 一些好看的页面
 *
 * @author cym    2021/12/22
 */
@Controller
public class TestController {
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
