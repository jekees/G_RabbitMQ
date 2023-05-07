package com.example.demo_rabbitmq.config;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayQueueConfig {
    public static final String DELAY_QUEUE="delay.queue";

    public static final String DELAY_EXCHANGE="delay.exchange";
    public static final String DELAY_ROUTINGKEY="delay.routingkey";

    @Bean
    public CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_EXCHANGE,"x-delayed-message",true,false,args);
    }
    @Bean
    public Queue delayedQueue(){
        return QueueBuilder.durable(DELAY_QUEUE).build();
    }

    @Bean
    public Binding delayQueueBindingDelayExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                  @Qualifier("delayedExchange") CustomExchange delayedExchange){
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAY_ROUTINGKEY).noargs();
    }
}
