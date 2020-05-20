package com.example.direct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageByDirectType() {
        String content = "This is a direct type of the RabbitMQ message example";
        this.rabbitTemplate.convertAndSend(
                "exchange.direct.example.new",
                "routing.key.direct.example.new",
                content);
    }



}
