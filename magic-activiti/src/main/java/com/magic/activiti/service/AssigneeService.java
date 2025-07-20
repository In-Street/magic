package com.magic.activiti.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *  动态分配负责人 assignee： uel模式、监听器模式
 *
 * @author Cheng Yufei
 * @create 2025-07-13 11:14
 **/
@Service
@Slf4j
public class AssigneeService {

    @Autowired
    @Lazy
    private RepositoryService repositoryService;
    @Autowired
    @Lazy
    private RuntimeService runtimeService;
    /**
     * 使用uel表达式分配责任人
     *
     * @return
     */
    public String uelAssigneeProcDef() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/listener-evection.bpmn20.xml")
                .addClasspathResource("bpmn/listener-evection.png")
                .deploy();

        log.info("监听器分配责任人流程定义部署完成，DeploymentId {}",deployment.getId());
        return "success";
    }

    /**
     *  uel 模式：
     *  启动流程实例，并制定负责人变量的值。
     *    可在 act_ru_variable 中，查看变量名及变量值
     *
     * @param deploymentId
     * @return
     */
    public String uelAssigneeProcInst(String deploymentId){

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey()
                , ImmutableMap.of("assignee0", "申请人A", "assignee1", "经理A", "assignee2", "总监A", "assignee3", "财务A"));

        log.info("流程实例部署完成，实例ID：{}",processInstance.getProcessInstanceId());
        return "success";
    }

    public String listenerAssigneeProcInst(String deploymentId){

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        log.info("监听器流程实例部署完成，实例ID：{}",processInstance.getProcessInstanceId());
        return "success";
    }
}
