package com.example.Java2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

