package com.example.demo_rabbitmq.comsumer;

import com.example.demo_rabbitmq.config.DelayQueueConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
@Log4j2
public class DelayConsumer {
    @RabbitListener(queues= DelayQueueConfig.DELAY_QUEUE)
    private void receive(Message msg) throws UnsupportedEncodingException {
        String msg1=new String(msg.getBody(),"UTF-8");
        log.info("当前时间：{}，接受到的消息为:{}",new Date().toString(),msg1);
    }
}
