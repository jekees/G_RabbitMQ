package com.example.demo_rabbitmq.Controller;

import com.example.demo_rabbitmq.config.ConfirmConfig;
import com.example.demo_rabbitmq.config.DelayQueueConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@Log4j2
public class Producer implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback{


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public  Producer(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate=rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @GetMapping("/sendMsg/{message}")
    public void process(@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条消息:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","xa","10s的队列："+message);
        rabbitTemplate.convertAndSend("X","xb","40s的队列："+message);
        rabbitTemplate.convertAndSend("X","xc","60s的队列："+message);
    }

    @GetMapping("/sendMsg/{message}/{ttl}")
    public void process(@PathVariable("ttl") String ttl,@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条延时为{}s的消息:{}",new Date().toString(),String.valueOf((Integer.parseInt(ttl)/1000)),message);
        rabbitTemplate.convertAndSend("X","xc",message,(var)->{
            var.getMessageProperties().setExpiration(ttl);
            return var;
        });
    }



    @GetMapping("/sendDelayMsg/{message}/{ttl}")
    public void process1(@PathVariable("ttl") String ttl,@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条延时为{}s的消息:{}",new Date().toString(),String.valueOf((Integer.parseInt(ttl)/1000)),message);
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE,DelayQueueConfig.DELAY_ROUTINGKEY,message,(var)->{
            var.getMessageProperties().setDelay(Integer.parseInt(ttl));
            return var;
        });
    }

    @GetMapping("/sendConfirmMsg/{message}/{ttl}")
    public void process3(@PathVariable("ttl") String ttl,@PathVariable("message") String message) {
        log.info("当前时间：{}，发送一条延时为{}s的消息:{}",new Date().toString(),String.valueOf((Integer.parseInt(ttl)/1000)),message);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,ConfirmConfig.CONFIRM_ROUTINGKEY,message,(var)->{
            var.getMessageProperties().setDelay(Integer.parseInt(ttl));
            return var;
        },new CorrelationData("1"));
    }

    @GetMapping("/sendConfirmMsg")
    public void process4() {
        log.info("当前时间：{}，发送一条延时为{}s的消息:{}",new Date().toString(),"20","hhh哈哈哈");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,ConfirmConfig.CONFIRM_ROUTINGKEY,"hhh哈哈哈",(var)->{
            var.getMessageProperties().setDelay(20000);
            return var;
        },new CorrelationData("1"));
        log.info("当前时间：{}，发送一条延时为{}s的消息:{}",new Date().toString(),"5","hhh嘿嘿嘿");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE,ConfirmConfig.CONFIRM_ROUTINGKEY+2,"hhh嘿嘿嘿",(var)->{
            var.getMessageProperties().setDelay(5000);
            return var;
        },new CorrelationData("2"));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id=correlationData!=null ? correlationData.getId() :"";
        if(b==true){
            log.info("当前时间：{}，交换机收到了ID为{}的消息",new Date().toString(),id);
        }else {
            log.info("交换机未收到消息，原因为：{}",s);
        }

    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {


        log.info("队列未收到消息：{}，路由交换机为：{}，原因为：{}",returnedMessage.getMessage(),returnedMessage.getExchange(),returnedMessage.getReplyText());
    }
}
