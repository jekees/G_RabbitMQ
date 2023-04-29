package com.example.demo_rabbitmq.rabbitmq01.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@Log4j2
public class Consumer {

    @RabbitListener(queues="QD")
    private void receive(Message msg) throws UnsupportedEncodingException {
        String msg1=new String(msg.getBody(),"UTF-8");
        log.info("当前时间：{} ，接受到的消息为：{}",new Date().toString(),msg1);
    }

}
