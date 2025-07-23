package com.magic.activiti.service;

import com.google.common.collect.ImmutableMap;
import com.magic.activiti.config.Evection;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *  global 流程变量使用
 * @author Cheng Yufei
 * @create 2025-07-21 12:08
 **/
@Service
@Slf4j
public class GlobalVariableService {

    @Autowired
    @Lazy
    private RepositoryService repositoryService;
    @Autowired
    @Lazy
    private RuntimeService runtimeService;

    public String deploymentGlobal(){

        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/global-evection.bpmn20.xml")
                .addClasspathResource("bpmn/global-evection.png").deploy();

        return deployment.getId();
    }

    /**
     *  在流程启动时，设置global变量
     *
     *   可在 ACT_RU_VARIABLE 表中查看变量
     * @param deploymentId
     * @param day
     * @return
     */
    public String startProcessInstanceGlobal(String deploymentId,Double day){

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();

        Evection evection = new Evection();
        evection.setDay(day);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), ImmutableMap.of("evection",evection ));

        return processInstance.getProcessInstanceId();
    }


}
