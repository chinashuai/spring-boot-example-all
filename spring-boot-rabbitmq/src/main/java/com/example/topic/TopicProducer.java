package com.example.topic;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessageByTopic() {
        String content = "This is a topic type of the RabbitMQ message example";
        this.rabbitTemplate.convertAndSend(
                "exchange.topic.example.new",
                "routing.key.example.new",
                content);
    }

}
