package com.magic.time.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-18 5:17 PM
 **/
@Configuration
@Slf4j
public class DbConfig {



    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource hikariDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        return hikariDataSource;
    }

        /*MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("com.zaxxer.hikari:type=Pool mbean");
        HikariPoolMXBean hikariPoolMXBean = JMX.newMBeanProxy(platformMBeanServer, objectName, HikariPoolMXBean.class);

        Connection connection = dataSource.getConnection();
        connection.close();*/

}
