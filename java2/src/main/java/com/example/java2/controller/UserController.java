package com.example.java2.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java2.pojo.User;
import com.example.java2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 *
 * @author cym
 * @since 2021-12-12
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String user(){
        return "User";
    }



    @GetMapping("/list")
    @ResponseBody
    public Map list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        Map<String, Object> returnMap  = new HashMap<>();
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage<User> data = userService.selectUserPage(page);
        returnMap.put("count", data.getTotal());
        returnMap.put("data", data.getRecords());
        return returnMap;
    }



//    @Autowired
//    private MQSender mqSender;
//
//    @GetMapping("/info")
//    @ResponseBody
//    public RespBean info(User user){
//         return RespBean.success(user);
//    }
//
//    @GetMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("222");
//    }
//    @GetMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01(){
//        mqSender.send("hello");
//    }
//    @GetMapping("/mq/direct01")
//    @ResponseBody
//    public void mq02(){
//        mqSender.send01("hello,Red");
//    }
//    @GetMapping("/mq/direct02")
//    @ResponseBody
//    public void mq03(){
//        mqSender.send02("hello,Green");
//    }
//    @GetMapping("/mq/topic01")
//    @ResponseBody
//    public void mq04(){
//        mqSender.send03("hello,Red");
//    }
//    @GetMapping("/mq/topic02")
//    @ResponseBody
//    public void mq05(){
//        mqSender.send04("hello,Green");
//    }
//    @GetMapping("/mq/topic")
//    @ResponseBody
//    public void mq06(){
//        mqSender.send05("hello,Red,Green");
//    }


}

