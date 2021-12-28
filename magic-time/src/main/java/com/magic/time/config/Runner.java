package com.magic.time.config;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-19 6:43 PM
 **/
@Component
@Slf4j
public class Runner implements ApplicationRunner {

    @Autowired
    private HikariDataSource hikariDataSource;

    /**
     * 延迟15秒后开始，hikari 在执行完一次sql后才会开始有pool的统计信息，否则一致不会产生
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();
            log.info("=========================");
            log.info("Active Connections: {}", hikariPoolMXBean.getActiveConnections());
            log.info("Total Connections: {}", hikariPoolMXBean.getTotalConnections());
            log.info("Threads Awaiting Connection: {}", hikariPoolMXBean.getThreadsAwaitingConnection());
            log.info("Idle Connections: {}", hikariPoolMXBean.getIdleConnections());
        }, 15, 1, TimeUnit.SECONDS);
    }
}
