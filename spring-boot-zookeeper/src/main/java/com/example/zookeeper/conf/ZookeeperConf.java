package com.example.zookeeper.conf;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperConf {

    @Value("${zk.url}")
    private String zkUrl;

    @Bean
    public CuratorFramework getCuratorFramework() {
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //创建客户端
//        CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
//                .connectString(zkUrl)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("zktest")
                .build();
        //建立连接
        client.start();
        return client;
    }
}