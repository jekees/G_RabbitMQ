package com.example.demo_rabbitmq.config;

import com.example.demo_rabbitmq.comsumer.Consumer;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_QUEUE="confirm.queue";
    public static final String CONFIRM_EXCHANGE="confirm.exchange";
    public static final String CONFIRM_ROUTINGKEY="confirm.routingkey";

//    @Bean
//    public CustomExchange confirmExchange(){
//        Map<String, Object> args = new HashMap<String, Object>();
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(CONFIRM_EXCHANGE,"x-delayed-message",true,false,args);
//    }

    @Bean
    public DirectExchange confirmExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE).durable(true).alternate(BackupConfig.BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }
    @Bean
    public Binding confirmQueueBindingConfirmExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                                      @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTINGKEY);
    }
}
