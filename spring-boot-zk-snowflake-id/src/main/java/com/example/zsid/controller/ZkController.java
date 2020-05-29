package com.example.zsid.controller;

import com.example.zsid.work.RegistryUidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZkController {

    @Autowired
    private RegistryUidGenerator registryUidGenerator;

    @RequestMapping("/zk/get")
    public void read(@RequestParam(value = "name") String name, @RequestParam(value = "len", required = false) Integer len) {

        len = len == null ? 3000 : len;

        for (int i = 0; i < len; i++) {
            long id = registryUidGenerator.getId(name);
            System.out.println(id);
//        7091947380736
            String s = registryUidGenerator.parseId(name, id);
            System.out.println(s);
        }

    }


}
