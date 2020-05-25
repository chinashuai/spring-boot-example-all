package com.example.zookeeper.controller;

import com.example.zookeeper.service.ZKlock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zk")
public class ZKCURDController {

    @Autowired
    private CuratorFramework client;

    public void create() throws Exception {
        //默认是持久节点
        client.create().forPath("/create/test1");
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/create/test2");
        client.create().forPath("/create/test3", "init".getBytes());
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/create/test4");
    }

    public void delete() throws Exception {

        client.delete().deletingChildrenIfNeeded().forPath("/delete/test1");
        //先创建在删除
        String path = "/delete/test2";
        client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        client.delete().withVersion(stat.getVersion()).forPath("/delete/test2");
        //强制保证删除，重试
        client.delete().guaranteed().forPath("/delete/test3");
    }

    public void read() throws Exception {
        //读取一个节点的数据内容
        client.getData().forPath("/get/test1");
        //获取一个节点数据，同时获取该节点的stat
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/get/test2");
    }

    public void update() throws Exception {
        //关系一个节点的数据内容
        client.setData().forPath("/update/test1", "test1-update".getBytes());
        //更新一个节点的数据内容，强制制定版本进行更新
        Stat stat = new Stat();
        client.setData().withVersion(stat.getVersion()).forPath("/update/test2", "test2-update".getBytes());
    }














//    @Autowired
//    private ZkClient zkClient;

    private String url = "127.0.0.1:2181";
    private int timeout = 3000;
    private String lockPath = "/testl";

    @Autowired
    private ZKlock zklock;

    private int k = 1;

    @GetMapping("/lock")
    public Boolean getLock() throws Exception {

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                zklock.lock();
                zklock.unlock();
            }).start();
        }
        return true;
    }
}
