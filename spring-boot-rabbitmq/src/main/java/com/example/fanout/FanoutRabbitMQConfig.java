package com.example.fanout;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutRabbitMQConfig {

    @Bean
    public Queue fanoutQueue() {
        return new Queue("queue.fanout.new");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("exchange.fanout.example.new");
    }

    @Bean
    Binding bindingFanoutExchangeC() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }


}
