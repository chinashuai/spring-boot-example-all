package com.example.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue.fanout.new")
public class FanoutConsumer {

    @RabbitHandler
    public void consumer(String message) {
        System.out.println(message);
    }

}
