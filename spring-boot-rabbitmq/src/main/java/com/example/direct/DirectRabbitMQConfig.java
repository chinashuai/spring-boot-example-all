package com.example.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DirectRabbitMQConfig {

    @Bean
    public Queue directQueue() {
        return new Queue("queue.direct.new");
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("exchange.direct.example.new");
    }

    @Bean
    Binding bindingTopicExchange() {
        return BindingBuilder
                .bind(directQueue())
                .to(directExchange())
                .with("routing.key.direct.example.new");
    }

}
