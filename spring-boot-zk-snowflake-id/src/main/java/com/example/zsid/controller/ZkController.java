package com.example.zsid.controller;


import com.example.zsid.util.Snowflake;
import com.example.zsid.work.RegistryUidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZkController {

    @Autowired
    private RegistryUidGenerator registryUidGenerator;

//    public static void main(String[] args) {
//
//        long id = new Snowflake(1, 1).nextId();
//        System.out.println(id);
//
//        // 十进制转二进制
//        String s = Long.toBinaryString(id);
//        System.out.println(s);
//
//    }

    @RequestMapping("/zk/get")
    public void read(String name) {


        for (int i = 0; i < 100; i++) {
            long id = registryUidGenerator.getId(name);
            System.out.println(id);
//        7091947380736

            String s = Long.toBinaryString(id);
            System.out.println(s);
        }

    }


}
