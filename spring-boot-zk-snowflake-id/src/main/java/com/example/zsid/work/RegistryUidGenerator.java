package com.example.zsid.work;


import com.example.zsid.uid.UidGenerator;
import com.example.zsid.uid.impl.CachedUidGenerator;
import com.example.zsid.uid.impl.DefaultUidGenerator;
import com.example.zsid.uid.worker.DisposableWorkerIdAssigner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@ConditionalOnBean(RegistryUidGenerator.class)
@Component
public class RegistryUidGenerator implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(RegistryUidGenerator.class);

    @Autowired
    private DisposableWorkerIdAssigner disposableWorkerIdAssigner;

    private static ApplicationContext context;

    /**
     *
     */
    private RegistryUidGenerator() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private static ConcurrentHashMap<String, UidGenerator> MAP = new ConcurrentHashMap<>();


    private static Set<String> BEAN_SET = Sets.newHashSet(
            "CacheUser", "DefaultUser"
    );
    /**
     * 刷新
     */
    private static volatile AtomicBoolean REFRESH_FLAG = new AtomicBoolean(false);

    public void refresh() {
        LOG.warn("RegistryUidGenerator refresh ....");
        long start = System.currentTimeMillis();
        while (!REFRESH_FLAG.compareAndSet(false, true)) {
            LOG.warn("其他线程 正在 refresh ....");
        }
        MAP.clear();

        synchronized (RegistryUidGenerator.class) {
            for (String beanName : BEAN_SET) {
                refreshBean(beanName);
            }
        }

        LOG.info("refresh done spend time = {} ms", (System.currentTimeMillis() - start));
        REFRESH_FLAG.compareAndSet(true, false);
    }

    /**
     * 指定bean name 刷新 bean，并缓存到map
     *
     * @param beanName
     */
    public void refreshBean(String beanName) {
        Preconditions.checkState(BEAN_SET.contains(beanName), "beanName = %s 没有配置唯一ID", beanName);
//
//        // Bean的实例工厂
//        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
//        // Bean构建 BeanService.class 要创建的Bean的Class对象
//        BeanDefinitionBuilder dataSourceBuider = BeanDefinitionBuilder.genericBeanDefinition(CachedUidGenerator.class);
//        // 向里面的属性注入值，提供get set方法
//        dataSourceBuider.addPropertyValue("boostPower", 3);
//        dataSourceBuider.addPropertyValue("paddingFactor", 10);
//        dataSourceBuider.addPropertyValue("workerIdAssigner", context.getBean("disposableWorkerIdAssigner"));
//        // dataSourceBuider.setParentName(""); 同配置 parent
//        // 同配置 scope
//        dataSourceBuider.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
//        // 将实例注册spring容器中 bs 等同于 id配置
//        dbf.registerBeanDefinition(beanName, dataSourceBuider.getBeanDefinition());
//        UidGenerator beanService = (CachedUidGenerator) context.getBean(beanName);
//
        long workId = disposableWorkerIdAssigner.assignWorkerId();

        // use CachedUidGenerator
        // CachedUidGenerator UidGenerator = new CachedUidGenerator(workId);

        // use DefaultUidGenerator
        DefaultUidGenerator UidGenerator = new DefaultUidGenerator(workId);

        if ("CacheUser".equalsIgnoreCase(beanName)) {
            UidGenerator = new CachedUidGenerator(workId);
        }

        MAP.put(beanName, UidGenerator);
    }

    /**
     * 根据bean name 获取 实例
     *
     * @param beanName
     * @return
     */
    public UidGenerator generateUidGenerator(String beanName) {
        Preconditions.checkState(BEAN_SET.contains(beanName), "beanName = %s 没有配置唯一ID", beanName);
        //刷新的过程中，等待刷新完毕
        while (REFRESH_FLAG.get()) {

        }
        if (MAP.containsKey(beanName)) {
            return MAP.get(beanName);
        }
        UidGenerator beanService = null;
        try {
            beanService = (UidGenerator) context.getBean(beanName);
        } catch (Exception e) {
            LOG.error("beanName={}不存在，需要创建", beanName, e);
        }
        if (beanService != null) {
            MAP.put(beanName, beanService);
            return beanService;
        }
        long start = System.currentTimeMillis();
        synchronized (RegistryUidGenerator.class) {
            if (beanService == null && MAP.get(beanName) == null) {
                LOG.warn("beanName={}不存在，需要创建", beanName);
                refreshBean(beanName);
            }

        }
        LOG.warn("generateUidGenerator beanName={}, spend time={}", beanName, (System.currentTimeMillis() - start));
        return MAP.get(beanName);
    }

    /**
     * 获取ID
     *
     * @param beanName
     * @return
     */
    public long getId(String beanName) {
        Preconditions.checkState(BEAN_SET.contains(beanName), "beanName = %s 没有配置唯一ID", beanName);
        UidGenerator uidGenerator = generateUidGenerator(beanName);
        if (uidGenerator != null) {
            try {
                return uidGenerator.getUID();
            } catch (Exception e) {
                LOG.error("uidGenerator.getUID() beanName = {} error", beanName, e);
                synchronized (RegistryUidGenerator.class) {
                    refreshBean(beanName);
                }
                return uidGenerator.getUID();

            }
        }
        throw new RuntimeException(String.format("beanName=%s 无对应的bean", beanName));
    }

    /**
     * parseId
     *
     * @param beanName
     * @param id
     * @return
     */
    public String parseId(String beanName, long id) {
        Preconditions.checkState(BEAN_SET.contains(beanName), "beanName = %s 没有配置唯一ID", beanName);
        UidGenerator uidGenerator = generateUidGenerator(beanName);
        if (uidGenerator != null) {
            try {
                return uidGenerator.parseUID(id);
            } catch (Exception e) {
                LOG.error("uidGenerator.parseUID() beanName = {} error", beanName, e);
                synchronized (RegistryUidGenerator.class) {
                    refreshBean(beanName);
                }
                return uidGenerator.parseUID(id);
            }
        }
        throw new RuntimeException(String.format("beanName=%s 无对应的bean", beanName));
    }

}
