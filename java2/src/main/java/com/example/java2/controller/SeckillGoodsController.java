package com.example.java2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.java2.pojo.Order;
import com.example.java2.pojo.SeckillMessage;
import com.example.java2.pojo.SeckillOrders;
import com.example.java2.pojo.User;
import com.example.java2.rabbitmq.MQSender;
import com.example.java2.service.IGoodsService;
import com.example.java2.service.IOrderService;
import com.example.java2.service.ISeckillOrdersService;
import com.example.java2.utils.JsonUtil;
import com.example.java2.vo.GoodsVO;
import com.example.java2.vo.RespBean;
import com.example.java2.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */

@Controller
@RequestMapping("/seckill")
public class SeckillGoodsController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrdersService iSeckillOrdersService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSender mqSender;


    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();

    @PostMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(Model model,User user, Long goodsId ){
        if (user == null){
            return RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        //判断是否重复抢购
        ValueOperations valueOperations = redisTemplate.opsForValue();
        SeckillOrders seckillOrders= (SeckillOrders) redisTemplate.opsForValue().get("order:"+user
                .getId()+":"+goodsId);
        //不为空意味着之前之前已经提交过订单
        if (seckillOrders !=null){
            return RespBean.error(RespBeanEnum.REPT_ERROR);
        }
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.COUNT_NOT);
        }
        //获取库存
        Long stock = valueOperations.decrement("seckillGoods"+goodsId);
        if (stock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods"+goodsId);
            return RespBean.error(RespBeanEnum.COUNT_NOT);
        }
        SeckillMessage seckillMessage = new SeckillMessage(user,goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);


        /*
        GoodsVO goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1){
              model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
              return RespBean.error(RespBeanEnum.COUNT_NOT);
        }
//        //判断是否重复抢购
//        SeckillOrders seckillOrders =
//                iSeckillOrdersService.getOne(new QueryWrapper <SeckillOrders>().eq(
//                "user_id",user.getId()).eq("goods_id",goodsId));

        SeckillOrders seckillOrders= (SeckillOrders) redisTemplate.opsForValue().get("order:"+user
                .getId()+":"+goodsId);
         //不为空意味着之前之前已经提交过订单
        if (seckillOrders !=null){
                model.addAttribute("errmsg",RespBeanEnum.REPT_ERROR.getMessage());
                return RespBean.error(RespBeanEnum.REPT_ERROR);
        }
        Order order = orderService.seckill(user,goods);
        return RespBean.success(order);*/



    }

    @RequestMapping(value = "/doSeckill2")
    public String doSeckill2(Model model, User user,Long goodsId ){
        if (user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVO goods = goodsService.findGoodsVoByGoodsId(goodsId);
        if ((goods.getStockCount() ==0)){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return "SeckillFail";
        }
        //判断是否重复抢购
        SeckillOrders seckillOrders =
                iSeckillOrdersService.getOne(new QueryWrapper <SeckillOrders>().eq(
                        "user_id",user.getId()).eq("goods_id",goodsId));
        //不为空意味着之前之前已经提交过订单
        if (seckillOrders !=null){
            model.addAttribute("errmsg",RespBeanEnum.REPT_ERROR);
            return "SeckillFail";
        }
        Order order = orderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";

    }

    /**
     * 系统初始化，把商品库存加载到Redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet ( ) throws Exception {
       List<GoodsVO> list=  goodsService.findGoodsVo();
       if (CollectionUtils.isEmpty(list)){
           return;
       }
       list.forEach(goodsVO -> {
           redisTemplate.opsForValue().set("seckillGoods"+goodsVO.getId(),goodsVO.getStockCount());
           EmptyStockMap.put(goodsVO.getId(),false);
       });
    }

    /**
     * 获取秒杀结果
     * @return orderId  是1成功，失败是-1 排队是0
     */
    @GetMapping("/result")
    @ResponseBody
    public RespBean result(User user,Long goodsId){
        if (user ==null){
            RespBean.error(RespBeanEnum.SEESION_ERROR);
        }
        Long orderId = iSeckillOrdersService.getResult(user,goodsId);
        return RespBean.success(orderId);

    }
}
