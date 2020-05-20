package com.example.config.rabbitmq;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 创建Queue、Exchange并建立绑定关系
 * Created by shuai on 2019/5/16.
 */
@Configuration
public class MyRabbitMQCreateConfig {

    @Resource(name = "v2RabbitAdmin")
    private RabbitAdmin v2RabbitAdmin;

    @Resource(name = "v1RabbitAdmin")
    private RabbitAdmin v1RabbitAdmin;

    @PostConstruct
    public void RabbitInit() {


//        v2RabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        v2RabbitAdmin.declareExchange(new TopicExchange("exchange.topic.example.new", true, false));
//        v2RabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

//        v2RabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        v2RabbitAdmin.declareQueue(new Queue("queue.example.topic.new", true));
//        v2RabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

//        v2RabbitAdmin.declareBinding(new Binding("test.direct.queue",
//                Binding.DestinationType.QUEUE,
//                "test.direct", "direct", new HashMap<>()));

        v2RabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue("queue.example.topic.new", true))        //直接创建队列
                        .to(new TopicExchange("exchange.topic.example.new", true, false))    //直接创建交换机 建立关联关系
                        .with("routing.key.example.new"));    //指定路由Key

//        v2RabbitAdmin.declareBinding(
//                BindingBuilder
//                        .bind(new Queue("test.fanout.queue", false))
//                        .to(new FanoutExchange("test.fanout", false, false)));

    }



}
