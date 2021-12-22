package com.example.java2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.java2.pojo.SeckillOrders;
import com.example.java2.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 * Cym
 * @author cym
 * @since 2021-12-14
 */
public interface ISeckillOrdersService extends IService<SeckillOrders> {


    Long getResult (User user,Long goodsId);
}
