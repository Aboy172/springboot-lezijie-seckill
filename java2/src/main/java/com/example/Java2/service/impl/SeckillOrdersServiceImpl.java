package com.example.Java2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Java2.mapper.SeckillOrdersMapper;
import com.example.Java2.pojo.SeckillOrders;
import com.example.Java2.pojo.User;
import com.example.Java2.service.ISeckillOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
public class SeckillOrdersServiceImpl extends ServiceImpl<SeckillOrdersMapper, SeckillOrders> implements ISeckillOrdersService {
    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Autowired
    private SeckillOrdersMapper seckillOrdersMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Long getResult (User user,Long goodsId) {

        SeckillOrders seckillOrders = seckillOrdersMapper.selectOne(new QueryWrapper<SeckillOrders>().eq("user_id",user.getId()).eq("goods_id",goodsId));
        if (!ObjectUtils.isEmpty(seckillOrders)) {
            return seckillOrders.getOrderId();
        } else if (redisTemplate.hasKey("isStockEmpty"+goodsId)) {
            return -1L;
        } else {
            return 0L;
        }




    }
}
