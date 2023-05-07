package com.example.demo_rabbitmq.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
public class RabbitConfig {

    private static final String X_EXCHANEG="X";
    private static final String Y_DEAD_LETTER_EXCHANEG="Y";
    private static final String QUEUE_A="QA";
    private static final String QUEUE_B="QB";

    private static final String QUEUE_C="QC";
    private static final String DEAD_LETTER_QUEUE_D="QD";

    @Bean
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANEG);
    }
    @Bean
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANEG);
    }

    @Bean
    public Queue queueA(){
        return QueueBuilder.durable(QUEUE_A).deadLetterExchange(Y_DEAD_LETTER_EXCHANEG).deadLetterRoutingKey("yd").ttl(10000).build();
    }

    @Bean
    public Queue queueB(){
        return QueueBuilder.durable(QUEUE_B).deadLetterExchange(Y_DEAD_LETTER_EXCHANEG).deadLetterRoutingKey("yd").ttl(40000).build();
    }

    @Bean
    public Queue queueC(){
        return QueueBuilder.durable(QUEUE_C).deadLetterExchange(Y_DEAD_LETTER_EXCHANEG).deadLetterRoutingKey("yd") .build();
    }

    @Bean
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }

    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange x_Exchange){
        return BindingBuilder.bind(queueA).to(x_Exchange).with("xa");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange x_Exchange){
        return BindingBuilder.bind(queueB).to(x_Exchange).with("xb");
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange x_Exchange){
        return BindingBuilder.bind(queueC).to(x_Exchange).with("xc");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange y_Exchange){
        return BindingBuilder.bind(queueD).to(y_Exchange).with("yd");
    }
}
