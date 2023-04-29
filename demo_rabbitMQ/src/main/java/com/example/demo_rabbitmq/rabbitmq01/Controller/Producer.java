package com.example.demo_rabbitmq.rabbitmq01.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@Log4j2
public class Producer {


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public  Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate=rabbitTemplate;
    }

    @GetMapping("/sendMsg/{message}")
    public void process(@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条消息{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","xa","10s的队列："+message);
        rabbitTemplate.convertAndSend("X","xb","40s的队列："+message);
    }

}
