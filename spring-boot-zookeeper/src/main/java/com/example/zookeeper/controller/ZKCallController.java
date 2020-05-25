package com.example.zookeeper.controller;

import com.example.zookeeper.conf.ZookeeperConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZKCallController {

    private CuratorFramework client = new ZookeeperConf().getCuratorFramework();

    private static CountDownLatch semaphore = new CountDownLatch(2);
    private static ExecutorService tp = Executors.newFixedThreadPool(2);

    /**
     * 测试 backgroundCall
     */
    public static void main(String[] args) throws Exception {
        new ZKCallController().backgroundCall();
    }

    /**
     * zookeeper中所有的一部通知事件处理都是EventThread这个线程来处理的，串行处理所有事件通知，以保证顺序
     * 但是如果遇到复杂的事件，处理时间过长，就会影响后续事件处理，于是可以单独创建一个线程池处理此类事件
     *
     * @throws Exception
     */
    public void backgroundCall() throws Exception {
        System.out.println("current thread ： " + Thread.currentThread().getName());
        //此处传入自定义 Executors
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) {
                        System.out.println("当前事件：event [code: " + curatorEvent.getResultCode() + " , type: " + curatorEvent.getType() + " ]");
                        System.out.println("当前线程：Thread of processResult: " + Thread.currentThread().getName());
                        semaphore.countDown();
                    }
                }
                , tp).forPath("/backgroundCall/test1", "init".getBytes());

        //此处不传入自定义的Executors
        client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).inBackground(
                new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) {
                        System.out.println("当前事件：event [code: " + curatorEvent.getResultCode() + " , type: " + curatorEvent.getType() + " ]");
                        System.out.println("当前线程：Thread of processResult: " + Thread.currentThread().getName());
                        semaphore.countDown();
                    }
                }).forPath("/backgroundCall/test1", "init".getBytes());

        semaphore.await();
        tp.shutdown();
    }

}
