package com.example.zsid.work;

import com.example.zsid.uid.worker.DisposableWorkerIdAssigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class ZKWorkIDConf implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(ZKWorkIDConf.class);

    public final static String ZK_ROOT_PATH = "/CoreAssetAccountID";
    public final static String ZK_ROOT_PATH_ID_GEN = "/CoreAssetAccountID/IdGen";
    public final static String ZK_ROOT_PATH_ID_GEN_WORD_ID_START = "ID-";

    @Value("${zk.url}")
    private String zkUrls;


    @Autowired
    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    @Autowired
    private RegistryUidGenerator registryUidGenerator;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZKWorkIdMaker workIdMaker = new ZKWorkIdMaker(zkUrls, ZK_ROOT_PATH_ID_GEN, ZK_ROOT_PATH_ID_GEN_WORD_ID_START, disposableWorkerIdAssigner, registryUidGenerator, countDownLatch);
        Thread thread = new Thread(() -> {
            try {
                workIdMaker.start();
                Thread.sleep(5000);

            } catch (Exception e) {
                log.error("start error", e);
            }
        });
        thread.start();
        countDownLatch.await();
        workIdMaker.refresh();
    }

}
