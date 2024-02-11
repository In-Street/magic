package com.magic.activiti.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author Cheng Yufei
 * @create 2024-02-09 16:49
 **/
@Configuration
public class ActivitiConfig {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 流程引擎配置对象
     * @param thirdDataSource
     * @return
     */
    @Bean(name="processEngineConfiguration")
    public ProcessEngineConfiguration processEngineConfiguration(@Qualifier("third")DataSource thirdDataSource){
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(thirdDataSource);
        configuration.setTransactionManager(platformTransactionManager);
        //true：若库中已存在相应的表，则直接使用，否则创建表
        configuration.setDatabaseSchemaUpdate("true");

        configuration.setJobExecutorActivate(false);
        configuration.setIdGenerator(new StrongUuidGenerator());

        configuration.setActivityFontName("宋体");
        configuration.setLabelFontName("宋体");
        configuration.setAnnotationFontName("宋体");
        return configuration;
    }


    /**
     * 流程引擎对象，可通过此对象获取各种服务组件
     * @param processEngineConfiguration
     * @return
     * @throws Exception
     */
    @Bean(name="processEngine")
    public ProcessEngine processEngine(@Qualifier("processEngineConfiguration") SpringProcessEngineConfiguration processEngineConfiguration) throws Exception {
        //getDefaultProcessEngine: 此方式默认从 activiti.cfg.xml 读取配置
        // ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();

        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean.getObject();
    }
}
