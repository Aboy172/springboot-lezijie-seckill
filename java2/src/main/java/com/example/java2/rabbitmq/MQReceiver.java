package com.example.java2.rabbitmq;

import com.example.java2.pojo.SeckillMessage;
import com.example.java2.pojo.SeckillOrders;
import com.example.java2.pojo.User;
import com.example.java2.service.IGoodsService;
import com.example.java2.service.IOrderService;
import com.example.java2.utils.JsonUtil;
import com.example.java2.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息消费者
 *
 * @author cym    2021/12/20
 */
@Service
@Slf4j
public class MQReceiver {


//    @RabbitListener(queues = "queue")
//    public void receive (Object msg){
//        log.info("接受的消息是"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("QUEUE1接受的信息是"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("QUEUE1接受的信息是"+msg);
//    }
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg){
//        log.info("Red接受的消息是"+msg);
//    }
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg){
//        log.info("Green接受的消息是"+msg);
//    }
//
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg){
//        log.info("QUEUE1接受的信息是"+msg);
//    }
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg){
//        log.info("QUEUE2接受的消息是"+msg);
//    }
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService iOrderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收到的消息是:"+message);
        SeckillMessage seckillMessage = JsonUtil.JsonStr2object(message,SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVO goodsVO = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVO.getStockCount() <1){
                return;
        }
        SeckillOrders seckillOrders= (SeckillOrders) redisTemplate.opsForValue().get("order:"+user
                .getId()+":"+goodsId);
        //不为空意味着之前之前已经提交过订单
        if (seckillOrders !=null){
            return;
        }
        iOrderService.seckill(user,goodsVO);


    }


}

