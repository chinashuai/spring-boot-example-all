package com.example.simple;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToQueue() {
        String content = "This is a simple type of the RabbitMQ message example";
        this.rabbitTemplate.convertAndSend("queue.simple.new", content);
    }

}
