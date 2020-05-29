package com.example.zsid.work;

import com.example.zsid.uid.UidGenerator;
import com.example.zsid.uid.utils.NetUtils;
import com.example.zsid.uid.worker.DisposableWorkerIdAssigner;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.zsid.work.ZKWorkIDConf.ZK_ROOT_PATH;

/**
 * Zookeeper的命名服务(ID生成器)
 */
public class ZKWorkIdMaker {
    private final static Logger log = LoggerFactory.getLogger(ZKWorkIdMaker.class);


    private CuratorFramework client = null;
    // 服务地址
    private final String zkUrls;
    // id生成器根节点
    private String root;
    // id节点
    private final String nodeName;
    // 启动状态: true:启动;false:没有启动，默认没有启动
    private volatile boolean running = false;

    private static ExecutorService cleanExector = new ThreadPoolExecutor(
            5,
            10,
            120L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100));


    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    private RegistryUidGenerator registryUidGenerator;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private CountDownLatch countDownLatchOuter = null;

    private static String localAddress = NetUtils.getLocalAddress();

//    private String rootPath = "/CoreAssetAccountID";


    public enum RemoveMethod {
        // 不，立即，延期
        NONE, IMMEDIATELY, DELAY
    }

    public ZKWorkIdMaker(String zkUrls, String root, String nodeName,
                         DisposableWorkerIdAssigner disposableWorkerIdAssigner,
                         RegistryUidGenerator registryUidGenerator, CountDownLatch countDownLatchOuter) {
        this.zkUrls = zkUrls;
        this.root = root;
        this.nodeName = nodeName;
        this.disposableWorkerIdAssigner = disposableWorkerIdAssigner;
        this.registryUidGenerator = registryUidGenerator;
        this.countDownLatchOuter = countDownLatchOuter;

    }

    /**
     * 启动
     *
     * @throws Exception
     */
    public void start() throws Exception {
        if (running) {
            throw new Exception("server has stated...");
        }
        running = true;
        init();
    }

    /**
     * 停止服务
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        if (!running) {
            throw new Exception("server has stopped...");
        }
        running = false;
        freeResource();
    }

    public void delete(String path) throws Exception {
        if (!running) {
            throw new Exception("server has stopped...");
        }
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    }

    private void init() {
        //RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        client = CuratorFrameworkFactory
                .builder()
                .connectString(zkUrls)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        String path = root;
        try {
            if (getNodeType(root) == null) {
                path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(root);
            }
        } catch (Exception e) {
            log.error("root = {} create error: ", path, e);
        }

        try {
            /**
             * {@link ZKWorkIDConf.ZK_ROOT_PATH} 下只有一个节点：{@link ZKWorkIDConf.ZK_ROOT_PATH_ID_GEN}，而 {@link ZKWorkIDConf.ZK_ROOT_PATH_ID_GEN} 下的子节点为持久自增节点，生成 workId
             * 此处监听 {@link ZKWorkIDConf.ZK_ROOT_PATH} 下的子节点 {@link ZKWorkIDConf.ZK_ROOT_PATH_ID_GEN}，当服务不断重启 workId 超出最大值则删除 {@link ZKWorkIDConf.ZK_ROOT_PATH_ID_GEN} 节点
             * 所有服务重新申请 workId
             *
             */
            final PathChildrenCache cache = new PathChildrenCache(client, ZK_ROOT_PATH, true);
            cache.getListenable().addListener(new PathChildrenCacheListener() {

                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                        throws Exception {
                    log.info("localAddress ={} 事件类型：event=" + event, localAddress);
                    if (event != null) {
                        log.info("localAddress ={} 事件类型：event.getType() = " + event.getType(), localAddress);
                    }

                    if (event.getData() != null) {
                        log.info("localAddress ={} 事件类型：" + event.getType() + "；操作节点：" + event.getData().getPath(), localAddress);
                    }

                    switch (event.getType()) {
                        case CHILD_ADDED:
                            log.info("localAddress ={} CHILD_ADDED:" + event.getData(), localAddress);
                            break;
                        case CHILD_UPDATED:
                            log.info("localAddress ={} CHILD_UPDATED:" + event.getData(), localAddress);
                            break;
                        case CONNECTION_LOST:
                            log.info("localAddress ={} CONNECTION_LOST:" + event.getData(), localAddress);
                            break;
                        case CONNECTION_SUSPENDED:
                            log.info("localAddress ={} CONNECTION_SUSPENDED:" + event.getData(), localAddress);
                            break;
                        case CONNECTION_RECONNECTED:
                            log.info("localAddress ={} CONNECTION_RECONNECTED:" + event.getData(), localAddress);
                            break;
                        case CHILD_REMOVED:
                            log.info("localAddress ={} CHILD_REMOVED:" + event.getData(), localAddress);

                            refresh();

                            break;
                        default:
                            break;
                    }
                }
            });
            log.info("localAddress ={}  {} 加入监听", localAddress, ZK_ROOT_PATH);
            cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            countDownLatchOuter.countDown();
            countDownLatch.await();

        } catch (Exception e) {
            log.error("节点已经存在,节点路径:" + root, e);
        }
    }

    public void refresh() throws Exception {
        String id = generateId(RemoveMethod.NONE);
        int workId = Integer.parseInt(id);
        log.info("localAddress ={} id={} , refresh workId = {}", localAddress, id, workId);


        if (workId > 999) {
            try {
                delete(root);
            } catch (Exception e) {
                log.error("delete error", e);
            }
        } else {
            synchronized (RegistryUidGenerator.class) {
                disposableWorkerIdAssigner.setWorkId(workId);
                registryUidGenerator.refresh();
            }

        }
    }

    /**
     * 资源释放 T
     */
    private void freeResource() {
        cleanExector.shutdown();
        try {
            cleanExector.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("localAddress ={} freeResource error", e);
        } finally {
            cleanExector = null;
        }
        if (client != null) {
            client.close();
            client = null;
        }
    }

    /**
     * 判断是否启动服务
     *
     * @throws Exception
     * @version 2016年11月29日上午9:39:58
     * @author wuliu
     */
    private void checkRunning() throws Exception {
        if (!running) {
            throw new Exception("请先调用start启动服务");
        }
    }

    /**
     * 提取ID
     *
     * @param str
     * @return
     */
    private String ExtractId(String str) {
        int index = str.lastIndexOf(nodeName);// 20
        if (index >= 0) {
            index += nodeName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    /**
     * 判断节点是否是持久化节点
     *
     * @param path 路径
     * @return null-节点不存在  | CreateMode.PERSISTENT-是持久化 | CreateMode.EPHEMERAL-临时节点
     */
    public CreateMode getNodeType(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);

            if (stat == null) {
                return null;
            }

            if (stat.getEphemeralOwner() > 0) {
                return CreateMode.EPHEMERAL;
            }

            return CreateMode.PERSISTENT;
        } catch (Exception e) {
            log.error("localAddress ={} getNodeType error", localAddress);
            return null;
        }
    }

    /**
     * 获取id
     *
     * @param removeMethod
     * @return
     * @throws Exception
     */
    public String generateId(RemoveMethod removeMethod) throws Exception {
        checkRunning();
        final String fullNodePath = root.concat("/").concat(nodeName);
        // 创建顺序节点每个父节点会为他的第一级子节点维护一份时序，会记录每个子节点创建的先后顺序。
        // 基于这个特性，在创建子节点的时候，可以设置这个属性，那么在创建节点过程中，
        // ZooKeeper会自动为给定节点名加上一个数字后缀，作为新的节点名
        String path = root;
        try {

            if (getNodeType(root) == null) {
                path = client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(root);
            }
        } catch (Exception e) {
            log.error("localAddress = {} , path = {} ,getNodeType error,", localAddress, path, e);
        }

        final String ourPath = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(fullNodePath, localAddress.getBytes());
        // 立即删除
        if (removeMethod.equals(RemoveMethod.IMMEDIATELY)) {

            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(ourPath);
        }
        // 延期删除
        else if (removeMethod.equals(RemoveMethod.DELAY)) {
            cleanExector.execute(() -> {
                try {
                    client.delete().guaranteed().deletingChildrenIfNeeded().forPath(ourPath);
                } catch (Exception e) {
                    log.error("localAddress = {} , deletingChildrenIfNeeded error", localAddress, e);
                }
            });

        }
        return ExtractId(ourPath);
    }

}