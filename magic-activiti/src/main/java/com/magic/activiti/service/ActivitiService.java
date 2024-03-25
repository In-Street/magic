package com.magic.activiti.service;


import cn.anony.annotations.ElementVersion;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
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
    @Autowired
    @Lazy
    private RuntimeService runtimeService;


    @ElementVersion
    private static final String version = "";

    public String deploy() {
        Deployment deployed = repositoryService.createDeployment()
                .addClasspathResource("bpmn/Aevection.bpmn20.xml")
                .addClasspathResource("bpmn/Aevection.bpmn20.png")
                .name("出差申请流程")
                .deploy();

        log.info("出差申请流程部署，id:{}",deployed.getId());
        return "success";
    }


    /**
     * 启动流程实例，参与表：
     *  act_hi_actinst：流程实例执行历史，一个 PROC_INST_ID_ 对应多条数据，包括：startEvent -> userTask 等 一个流程上的节点，并包含每个节点的startTime、endTime、assignee\
     *  act_hi_identitylink: 流程实例参与用户历史信息,
     *  act_hi_procinst： 流程实例历史信息，一个 PROC_INST_ID_ 对应一条数据，一个流程整体的 startTime、 endTime
     *  act_hi_taskinst： 流程任务历史信息
     *
     *  act_ru_execution ： 流程正在执行的信息，可有 ACT_ID_ 关联到 act_hi_actinst
     *  act_ru_identitylink：流程的参与用户信息
     *  act_ru_task:   流程正在执行的任务信息
     *
     * @return
     */
    public String startProcess() {
        //key: ACT_RE_PROCDEF # KEY_  字段
        ProcessInstance myEvectionProcessInstance = runtimeService.startProcessInstanceByKey("myEvection");

        // ACT_RE_PROCDEF # ID_ 字段
        log.info("流程定义ID：{}",myEvectionProcessInstance.getProcessDefinitionId());

        //与 getProcessInstanceId 一样， ACT_RU_TASK # PROC_INST_ID_ 字段
        log.info("流程实例ID：{}",myEvectionProcessInstance.getId());
        log.info("流程实例ID：{}",myEvectionProcessInstance.getProcessInstanceId());

        return "success";
    }
}
