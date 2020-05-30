package com.example.test;

import com.example.zsid.ZKSnowFlakeIdApplication;
import com.example.zsid.work.RegistryUidGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZKSnowFlakeIdApplication.class})
public class ZKSnowFlakeIdTest {

    @Autowired
    private RegistryUidGenerator registryUidGenerator;

    @Test
    public void testZKCreateSnowFlakeId() {
        int len = 3000;
        String name = "DefaultUser";
        //String name = "CacheUser";

        for (int i = 0; i < len; i++) {
            System.out.println("testZKCreateSnowFlakeId 第 " + i + " 次生成 id ：");
            long id = registryUidGenerator.getId(name);
            System.out.println(id);

            String s = registryUidGenerator.parseId(name, id);
            System.out.println(s);

            System.out.println();
        }

    }
}