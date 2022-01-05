package com.example.java2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.java2.config.AccessLimit;
import com.example.java2.exception.GlobalException;
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
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 前端控制器 Cym
 *
 * @author cym
 * @since 2021-12-14
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

  @Autowired private IGoodsService goodsService;
  @Autowired private ISeckillOrdersService iSeckillOrdersService;
  @Autowired private IOrderService orderService;
  @Autowired private RedisTemplate redisTemplate;
  @Autowired private MQSender mqSender;
  @Autowired private DefaultRedisScript<Long> script;

  private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

  @PostMapping("/{path}/doSeckill")
  @ResponseBody
  public RespBean doSeckill(@PathVariable("path") String path, User user, Long goodsId) {
    if (user == null) {
      return RespBean.error(RespBeanEnum.SEESION_ERROR);
    }
    // 判断是否重复抢购
    ValueOperations valueOperations = redisTemplate.opsForValue();
    Boolean check = orderService.checkPath(user, goodsId, path);
    if (!check) {
      return RespBean.error(RespBeanEnum.REQUEST_ERROR);
    }
    SeckillOrders seckillOrders =
        (SeckillOrders) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
    // 不为空意味着之前之前已经提交过订单
    if (seckillOrders != null) {
      return RespBean.error(RespBeanEnum.REPT_ERROR);
    }
    //减少对Redis的访问
    if (EmptyStockMap.get(goodsId)) {
      return RespBean.error(RespBeanEnum.COUNT_NOT);
    }
    // 获取库存
    //Long stock = valueOperations.decrement("seckillGoods" + goodsId);
    Long stock = (Long) redisTemplate.execute(script,Collections.singletonList("seckillGoods"+goodsId),Collections.EMPTY_LIST);
    if (stock < 0) {
      EmptyStockMap.put(goodsId, true);
      valueOperations.increment("seckillGoods" + goodsId);
      return RespBean.error(RespBeanEnum.COUNT_NOT);
    }
    SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
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
  //
  @RequestMapping(value = "/doSeckill2")
  public String doSeckill2(Model model, User user, Long goodsId) {
    if (user == null) {
      return "login";
    }
    model.addAttribute("user", user);
    GoodsVO goods = goodsService.findGoodsVoByGoodsId(goodsId);
    if ((goods.getStockCount() == 0)) {
      model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
      return "SeckillFail";
    }
    // 判断是否重复抢购
    SeckillOrders seckillOrders =
        iSeckillOrdersService.getOne(
            new QueryWrapper<SeckillOrders>().eq("user_id", user.getId()).eq("goods_id", goodsId));
    // 不为空意味着之前之前已经提交过订单
    if (seckillOrders != null) {
      model.addAttribute("errmsg", RespBeanEnum.REPT_ERROR);
      return "SeckillFail";
    }
    Order order = orderService.seckill(user, goods);
    model.addAttribute("order", order);
    model.addAttribute("goods", goods);
    return "orderDetail";
  }

  /**
   * 系统初始化，把商品库存加载到Redis中
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<GoodsVO> list = goodsService.findGoodsVo();
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    list.forEach(
        goodsVO -> {
          redisTemplate
              .opsForValue()
              .set("seckillGoods" + goodsVO.getId(), goodsVO.getStockCount());
          EmptyStockMap.put(goodsVO.getId(), false);
        });
  }

  /**
   * 获取秒杀结果
   *
   * @return orderId 是1成功，失败是-1 排队是0
   */
  @GetMapping("/result")
  @ResponseBody
  public RespBean result(User user, Long goodsId) {
    if (user == null) {
      RespBean.error(RespBeanEnum.SEESION_ERROR);
    }
    Long orderId = iSeckillOrdersService.getResult(user, goodsId);
    return RespBean.success(orderId);
  }

  /**
   * 功能描述:秒杀地址
   *
   * @param user
   * @param goodsId
   * @return
   */
  @AccessLimit(second = 3, maxCount = 3)
  @GetMapping("/path")
  @ResponseBody
  public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
    if (user == null) {
      return RespBean.error(RespBeanEnum.SEESION_ERROR);
    }
    boolean check = orderService.checkCpatcha(user, goodsId, captcha);
    if (!check) {
      return RespBean.error(RespBeanEnum.CAPTCHA_ERROR);
    }
    String str = orderService.createPath(user, goodsId);
    return RespBean.success(str);
  }

  /**
   * 生成验证码
   *
   * @param user
   * @param goodsId
   * @param response
   */
  @GetMapping("/captcha")
  public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
    if (user == null || goodsId < 0) {
      throw new GlobalException(RespBeanEnum.SEESION_ERROR);
    }
    response.setContentType("image/gif");
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    // 生成验证码
    ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 28, 3);
    redisTemplate
        .opsForValue()
        .set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 200, TimeUnit.SECONDS);
    try {
      captcha.out(response.getOutputStream());
    } catch (IOException e) {
      log.info("验证码验证失败！", e.getMessage());
    }
  }
}
