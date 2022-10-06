package com.example.order.controller;

import com.example.order.entity.OrderEntity;
import com.example.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author: very_modest
 * @date: 2022/9/18 23:28
 * @description:
 */

@Slf4j
@Controller
@RabbitListener(queues = "hello-java-queue")
public class RabbitController {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/rabbit/send")
    public void sendMessageTest(@RequestParam(value = "num",required = false,defaultValue = "10") Integer num) {
        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
        OrderEntity orderEntity = new OrderEntity();
        for (int i = 0; i < num; i++) {
            if(i % 2 == 0){
                reasonEntity.setId((long) i);
                reasonEntity.setCreateTime(new Date());
                reasonEntity.setName("reason");
                reasonEntity.setStatus(1);
                reasonEntity.setSort(2);
                String msg = "Hello World";
                //1、发送消息,如果发送的消息是个对象，会使用序列化机制，将对象写出去，对象必须实现Serializable接口
                //2、发送的对象类型的消息，可以是一个json
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",
                        reasonEntity,new CorrelationData(UUID.randomUUID().toString()));
                log.info("消息发送完成:{}",reasonEntity);
            }else{
                orderEntity.setId((long) i);
                orderEntity.setCreateTime(new Date());
                orderEntity.setNote("订单"+i);
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",
                        orderEntity,new CorrelationData(UUID.randomUUID().toString()));
                log.info("消息发送完成:{}",orderEntity);
            }
        }
    }



    /**
     * queues:声明需要监听的所有队列
     *
     * org.springframework.amqp.core.Message
     * 参数可以写以下类型：
     *
     * @param message 原生消息详细信息。头+体
     * @param content T<发送的消息的类型> OrderReturnReasonEntity content
     * @param channel Channel channel: 当前传输数据的通道
     *
     * Queue: 可以很多人都来监听。只要收到消息，队列删除消息，而且只能有一个收到此消息
     *   1）、订单服务启动多个：同一个消息，只能有一个客户端收到
     *   2）、只有一个消息完全处理完，方法运行结束，我们就可以收到下一个消息
     */
    @RabbitHandler
    public void receiveMessage(Message message, OrderReturnReasonEntity content, Channel channel){
        //拿到主体内容
        byte[] body = message.getBody();
        //拿到的消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        try {
            //ack
            channel.basicAck(deliveryTag,true);

            //notAck
//            channel.basicReject(deliveryTag,false);
//            channel.basicNack(deliveryTag,false,false);
        } catch (IOException e) {
            //网络中断
            e.printStackTrace();
        }

        System.out.println("接受到的消息3...内容" + message + "===内容：" + content);
    }

    @RabbitHandler
    public void receiveMessage2(Message message, OrderEntity content, Channel channel){
        System.out.println("接受到的消息4...内容" + content + "===内容：" + content);
    }
}
