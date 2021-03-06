package com.example.java2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java2.pojo.Order;
import com.example.java2.pojo.User;
import com.example.java2.vo.GoodsVO;
import com.example.java2.vo.orderDetailVo;

/**
 * 服务类 Cym
 *
 * @author cym
 * @since 2021-12-14
 */
public interface IOrderService extends IService<Order> {

  Order seckill(User user, GoodsVO goods);

  orderDetailVo detail(Long orderId);

  String createPath(User user, Long goodsId);

  Boolean checkPath(User user, Long goodsId, String path);

  Boolean checkCpatcha(User user, Long goodsId, String captcha);
}
