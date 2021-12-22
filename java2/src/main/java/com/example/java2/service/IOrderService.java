package com.example.Java2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.Java2.pojo.Order;
import com.example.Java2.pojo.User;
import com.example.Java2.vo.GoodsVO;
import com.example.Java2.vo.orderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */
public interface IOrderService extends IService<Order> {


   Order seckill (User user, GoodsVO goods);


    orderDetailVo detail (Long orderId);
}
