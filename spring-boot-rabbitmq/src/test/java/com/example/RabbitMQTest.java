package com.example;

import com.example.direct.DirectProducer;
import com.example.fanout.FanoutProducer;
import com.example.simple.SimpleProducer;
import com.example.topic.TopicProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private DirectProducer directProducer;
    @Autowired
    private FanoutProducer fanoutProducer;
    @Autowired
    private SimpleProducer simpleProducer;
    @Autowired
    private TopicProducer topicProducer;


    @Test
    public void directProducerTest() {
        directProducer.sendMessageByDirectType();
    }

    @Test
    public void fanoutProducerTest() {
        fanoutProducer.sendMessageByFanoutType();
    }

    @Test
    public void simpleProducerTest() {
        simpleProducer.sendToQueue();
    }

    @Test
    public void topicProducerTest() {
        topicProducer.sendMessageByTopic();
    }


}
