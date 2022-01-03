package com.example.java2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.java2.exception.GlobalException;
import com.example.java2.mapper.OrderMapper;
import com.example.java2.pojo.Order;
import com.example.java2.pojo.SeckillGoods;
import com.example.java2.pojo.SeckillOrders;
import com.example.java2.pojo.User;
import com.example.java2.service.IGoodsService;
import com.example.java2.service.IOrderService;
import com.example.java2.service.ISeckillGoodsService;
import com.example.java2.service.ISeckillOrdersService;
import com.example.java2.utils.MD5Util;
import com.example.java2.utils.UUIDUtil;
import com.example.java2.vo.GoodsVO;
import com.example.java2.vo.RespBeanEnum;
import com.example.java2.vo.orderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 * Cym
 *
 * @author cym
 * @since 2021-12-14
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private ISeckillGoodsService iSeckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrdersService iSeckillOrdersService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order seckill (User user,GoodsVO goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品减库存
        SeckillGoods seckillGoods = iSeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq
                ("goods_id",goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

        iSeckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql(
                        "stock_count="+"stock_count -1")
                .eq("goods_id",goods.getId()).gt("stock_count",0));
        if (seckillGoods.getStockCount() < 1) {
            //判断是否有库存
            valueOperations.set("isStockEmpty"+goods.getId(),"0");
            return null;
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliverAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreadateDate(new Date());
        orderMapper.insert(order);
        SeckillOrders seckillOrders = new SeckillOrders();
        seckillOrders.setOrderId(order.getId());
        seckillOrders.setGoodsId(goods.getId());
        seckillOrders.setUserId(user.getId());
        iSeckillOrdersService.save(seckillOrders);
        redisTemplate.opsForValue().set("order:"+user
                .getId()+":"+goods.getId(),seckillOrders);
        return order;
    }

    @Override
    public orderDetailVo detail (Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDERID_NOT);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVO goodsVO = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        orderDetailVo detail = new orderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVO(goodsVO);
        return detail;
  }

  /**
   * 生成秒杀地址
   *
   * @param user
   * @param goodsId
   * @return
   */
  @Override
  public String createPath(User user, Long goodsId) {
    String str = MD5Util.md5(UUIDUtil.uuid() + "ABIDE");
    redisTemplate
        .opsForValue()
        .set("seckillPath:" + user.getId() + ":" + goodsId, str, 120, TimeUnit.SECONDS);
    return str;
  }

  /**
   * 检验秒杀地址
   *
   * @param user
   * @param goodsId
   * @param path
   * @return
   */
  @Override
  public Boolean checkPath(User user, Long goodsId, String path) {
    if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
      return false;
    }
    String redisPath =
        (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
    return path.equals(redisPath);
  }

  /**
   * 检验验证码
   *
   * @return
   * @param user
   * @param goodsId
   * @param captcha
   */
  @Override
  public Boolean checkCpatcha(User user, Long goodsId, String captcha) {
    if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
      return false;
    }
    ValueOperations valueOperations = redisTemplate.opsForValue();
    String redisCaptcha = ((String) valueOperations.get("captcha:" + user.getId() + ":" + goodsId));

    return captcha.equals(redisCaptcha);
  }
}
