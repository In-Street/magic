package com.magic.activiti.config;

import org.activiti.engine.*;
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
    @Qualifier("thirdPlatformTransactionManager")
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

    /**
     *  通过 ProcessEngine 获取服务组件Bean
     * @param processEngine
     * @return
     */
    @Bean(name="repositoryService")
    public RepositoryService repositoryService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getRepositoryService();
    }

    @Bean(name="runtimeService")
    public RuntimeService runtimeService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getRuntimeService();
    }

    @Bean(name="historyService")
    public HistoryService historyService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getHistoryService();
    }

    @Bean(name="taskService")
    public TaskService taskService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getTaskService();
    }

    @Bean(name="identityService")
    public IdentityService identityService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getIdentityService();
    }

    @Bean(name="managementService")
    public ManagementService managementService(@Qualifier("processEngine") ProcessEngine processEngine){
        return processEngine.getManagementService();
    }
}
