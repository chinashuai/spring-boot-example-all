package com.example.topic;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TopicProducer {

    @Resource(name = "v1RabbitTemplate")
    private RabbitTemplate v1RabbitTemplate;

    @Resource(name = "v2RabbitTemplate")
    private RabbitTemplate v2RabbitTemplate;

    public void sendMessageByTopic() {
        String content1 = "This is a topic type of the RabbitMQ message example from v1RabbitTemplate";
        v1RabbitTemplate.convertAndSend(
                "exchange.topic.example.new",
                "routing.key.example.new",
                content1);

        String content2 = "This is a topic type of the RabbitMQ message example from v2RabbitTemplate";
        v2RabbitTemplate.convertAndSend(
                "exchange.topic.example.new",
                "routing.key.example.new",
                content2);
    }

}
