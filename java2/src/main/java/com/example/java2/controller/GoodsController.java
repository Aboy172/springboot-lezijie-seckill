//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.java2.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.java2.pojo.User;
import com.example.java2.service.IGoodsService;
import com.example.java2.service.IUserService;
import com.example.java2.vo.DetailVo;
import com.example.java2.vo.GoodsVO;
import com.example.java2.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * windows优化前qps:1642
 *             缓存:2596
 * Linux优化前qps：2246
 */

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @GetMapping(value = "/toList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response,Model model,User user) {
        //从Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        //如果为空，用thymeleafViewResolver手动渲染页面，并且返回页面
        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60,TimeUnit.SECONDS);
        }
        return html;
        }

    @RequestMapping( "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user,@PathVariable Long goodsId){
        GoodsVO goodsVO  = goodsService.findGoodsVoByGoodsId(goodsId);
        Date endDate = goodsVO.getEndDate();
        Date startDate = goodsVO.getStartDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;

        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if(nowDate.before(startDate)){
            remainSeconds = ((int)(startDate.getTime()-nowDate.getTime())/1000);
        }
        //秒杀结束
        else if(nowDate.after(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }
        //秒杀进行中
        else{
            seckillStatus = 1;
            remainSeconds = 0;
        }


        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVO(goodsVO);
        detailVo.setUser(user);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(seckillStatus);
        return RespBean.success(detailVo);

    }

    @RequestMapping( value = "/toDetail2/{goodId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(HttpServletRequest request,HttpServletResponse response,Model model,User user,@PathVariable Long goodsId){


        //Redis中获取页面，如果不为空，直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail" + goodsId);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        GoodsVO goodsVO  = goodsService.findGoodsVoByGoodsId(goodsId);
        Date endDate = goodsVO.getEndDate();
        Date startDate = goodsVO.getStartDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;

        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀未开始
        if(nowDate.before(startDate)){
            remainSeconds = ((int)(startDate.getTime()-nowDate.getTime())/1000);
        }
        //秒杀结束
        else if(nowDate.after(endDate)){
            seckillStatus = 2;
            remainSeconds = -1;
        }
        //秒杀进行中
        else{
            seckillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("goods",goodsVO);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("SecKillStatus", seckillStatus);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);

        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;

    }
}


