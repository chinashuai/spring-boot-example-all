package com.example.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.direct.new")
public class DirectConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.println(message);
    }

}
