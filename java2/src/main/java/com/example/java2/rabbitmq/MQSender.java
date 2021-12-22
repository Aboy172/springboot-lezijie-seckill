package com.example.java2.rabbitmq;

import com.example.java2.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 *
 * @author cym    2021/12/20
 */

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg){
//        //发送消息
//        log.info("发送的消息是:"+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//    public void send01(Object msg){
//        log.info("发送的消息是:"+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
//
//    }
//    public void send02(Object msg){
//        log.info("发送给Green的消息是"+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
//    }
//    public void send03(Object msg){
//        log.info("发送给QUEUE01（QUEUE01接收）"+msg);
//        rabbitTemplate.convertAndSend("topicExchange","rabbitmq.orange.queue",msg);
//    }
//    public void send04(Object msg){
//        log.info("发送给QUEUE02（QUEUE02接收）"+msg);
//        rabbitTemplate.convertAndSend("topicExchange","orange.queue",msg);
//    }
//    public void send05(Object msg){
//        log.info("发送给两个队列的消息是:"+msg);
//        rabbitTemplate.convertAndSend("topicExchange","orange.orange.rabbitmq",msg);
//    }
        public void sendSeckillMessage(String message){
            log.info("发送的消息是"+message);
            rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
            String s = JsonUtil.object2JsonStr(message);


        }























}
