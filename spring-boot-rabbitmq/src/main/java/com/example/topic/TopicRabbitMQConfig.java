package com.example.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * create rabbitmq queue exchange and bind by routingKey
 */
@Configuration
public class TopicRabbitMQConfig {

    @Bean
    public Queue topicQueue() {
        return new Queue("queue.example.topic.new");
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("exchange.topic.example.new");
    }

    @Bean
    Binding bindingTopicExchange() {
        return BindingBuilder
                .bind(topicQueue())
                .to(topicExchange())
                .with("routing.key.example.new");
    }

}
