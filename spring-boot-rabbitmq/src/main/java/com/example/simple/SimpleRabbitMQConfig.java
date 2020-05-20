package com.example.simple;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SimpleRabbitMQConfig {

    @Bean
    public Queue simpleQueue() {
        return new Queue("queue.simple.new");
    }

}
