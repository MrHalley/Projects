package com.example.order;

import com.example.order.entity.OrderEntity;
import com.example.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@Slf4j
@SpringBootTest
class OrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void sendMessageTest() {
        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
        OrderEntity orderEntity = new OrderEntity();
        for (int i = 0; i < 10; i++) {
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
            }else{
                orderEntity.setId((long) i);
                orderEntity.setCreateTime(new Date());
                orderEntity.setNote("订单"+i);
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",
                        orderEntity,new CorrelationData(UUID.randomUUID().toString()));
            }
            log.info("消息发送完成:{}",reasonEntity);
        }
    }

    /**
     * 1、如何创建Exchange、Queue、Binding
     *      1）、使用AmqpAdmin进行创建
     * 2、如何收发消息
     */
    @Test
    public void createExchange() {
        Exchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功：","hello-java-exchange");
    }



    @Test
    public void testCreateQueue() {
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功：","hello-java-queue");
    }


    @Test
    public void createBinding() {
        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello.java",
                null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}]创建成功：","hello-java-binding");
    }
}
