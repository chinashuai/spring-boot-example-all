package com.example.test;

import com.example.redis.RedisDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisDao redisDao;

    @Test
    public void testRedis() {
        redisDao.setKey("url","http://www.chinaxieshuai.com");
        System.out.println(redisDao.getValue("url"));
    }

}
