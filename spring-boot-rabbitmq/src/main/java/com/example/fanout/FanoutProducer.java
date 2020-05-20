package com.example.fanout;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FanoutProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageByFanoutType() {
        String content = "This is a fanout type of the RabbitMQ message example";
        this.rabbitTemplate.convertAndSend(
                "exchange.fanout.example.new",
                "",
                content);
    }

}
