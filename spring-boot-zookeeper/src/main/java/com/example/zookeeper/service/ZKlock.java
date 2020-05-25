package com.example.zookeeper.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component("zklock")
public class ZKlock implements Lock {

    @Autowired
    private CuratorFramework zkClient;
    @Value("${zk.localPath}")
    private String lockPath;
    private String currentPath;
    private String beforePath;

    @Override
    public boolean tryLock() {
        try {
            //根节点的初始化放在构造函数里面不生效
            if (zkClient.checkExists().forPath(lockPath) == null) {
                System.out.println("初始化根节点==========>" + lockPath);
                zkClient.create().creatingParentsIfNeeded().forPath(lockPath);
            }
            System.out.println("当前线程" + Thread.currentThread().getName() + "初始化根节点" + lockPath);
        } catch (Exception e) {
        }

        if (currentPath == null) {
            try {
                currentPath = this.zkClient.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                        .forPath(lockPath + "/");
            } catch (Exception e) {
                return false;
            }
        }
        try {
            //此处该如何获取所有的临时节点呢？如locks00004.而不是获取/locks/order中的order作为子节点？？
            List<String> childrens = this.zkClient.getChildren().forPath(lockPath);
            Collections.sort(childrens);
            if (currentPath.equals(lockPath + "/" + childrens.get(0))) {
                System.out.println("当前线程获得锁" + currentPath);
                return true;
            } else {
                //取前一个节点
                int curIndex = childrens.indexOf(currentPath.substring(lockPath.length() + 1));
                //如果是-1表示children里面没有该节点
                beforePath = lockPath + "/" + childrens.get(curIndex - 1);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            waiForLock();
            lock();
        }
    }

    @Override
    public void unlock() {
        try {
            zkClient.delete().guaranteed().deletingChildrenIfNeeded().forPath(currentPath);
        } catch (Exception e) {
            //guaranteed()保障机制，若未删除成功，只要会话有效会在后台一直尝试删除
        }
    }

    private void waiForLock() {
        CountDownLatch cdl = new CountDownLatch(1);
        //创建监听器watch
        NodeCache nodeCache = new NodeCache(zkClient, beforePath);
        try {
            nodeCache.start(true);
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    cdl.countDown();
                    System.out.println(beforePath + "节点监听事件触发，重新获得节点内容为：" + new String(nodeCache.getCurrentData().getData()));
                }
            });
        } catch (Exception e) {
        }
        //如果前一个节点还存在，则阻塞自己
        try {
            if (zkClient.checkExists().forPath(beforePath) == null) {
                cdl.await();
            }
        } catch (Exception e) {

        } finally {
            //阻塞结束，说明自己是最小的节点，则取消watch，开始获取锁
            try {
                nodeCache.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

}