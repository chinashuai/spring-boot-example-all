package com.example.simple;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.simple.new")
public class SimpleConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.println(message);
    }

}
