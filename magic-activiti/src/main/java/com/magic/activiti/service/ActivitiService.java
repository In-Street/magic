package com.magic.activiti.service;


import cn.anony.annotations.ElementVersion;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2024-02-14 18:35
 **/
@Service
@Slf4j
public class ActivitiService {

    @Autowired
    @Lazy
    private RepositoryService repositoryService;


    @ElementVersion
    private static final String version = "";

    public String deploy() {
        Deployment deployed = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Aevection.bpmn20.xml")
                .addClasspathResource("bpmn/Aevection.bpmn20.png")
                .name("出差申请流程")
                .deploy();

        log.info("出差申请流程部署，id:{}",deployed.getId());
        System.out.printf("出差申请流程部署，id:%s，测试合并分支部分文件AA",deployed.getId());
        System.out.printf("出差申请流程部署，id:%s，测试合并分支部分文件BB",deployed.getId());
        return "success";
    }
}
