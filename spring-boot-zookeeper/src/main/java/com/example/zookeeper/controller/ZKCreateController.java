package com.example.zookeeper.controller;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class ZKCreateController {


    static String root = "/zk";
    static String idStr = "ID";

    public static void main(String[] args) throws Exception {
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端
//        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
//                .connectString(zkUrl)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("shuai")
                .build();
        //建立连接
        client.start();
        Stat stat = client.checkExists().forPath(root);
        System.out.println(stat);

        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(root);

        String fullNodePath = root.concat("/").concat(idStr);
        String ourPath = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(fullNodePath, "localAddress".getBytes());
        System.out.println(ourPath);
    }




}
