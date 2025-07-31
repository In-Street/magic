package com.magic.activiti.service;

import com.google.common.collect.ImmutableMap;
import com.magic.activiti.config.Evection;
import lombok.extern.slf4j.Slf4j;
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
 * 网关
 *
 * @author Cheng Yufei
 * @create 2025-07-25 11:47
 **/
@Service
@Slf4j
public class GatewayService {

    @Autowired
    @Lazy
    private RepositoryService repositoryService;
    @Autowired
    @Lazy
    private RuntimeService runtimeService;
    @Autowired
    @Lazy
    private TaskService taskService;


    /**
     *   定义流程，开启流程
     * @param whetherDeployment
     * @param day
     * @param gatewayType       :  gateway-exclusive 、 gateway-parallel 、gateway-inclusive
     * @return
     */
    public String deploymentAndStartProc(boolean whetherDeployment, Double day, String gatewayType) {

        Deployment deployment;
        if (whetherDeployment) {
            switch (gatewayType){
                case "gateway-exclusive": // 排他网关
                    deployment = repositoryService.createDeployment().addClasspathResource("bpmn/gateway-exclusive.bpmn20.xml").deploy();
                    break;
                case "gateway-parallel": // 并行网关

                    deployment = repositoryService.createDeployment().addClasspathResource("bpmn/gateway-parallel.bpmn20.xml").deploy();
                    break;

                case "gateway-inclusive": // 包含网关

                    deployment = repositoryService.createDeployment().addClasspathResource("bpmn/gateway-inclusive.bpmn20.xml").deploy();
                    break;

                default:
                    return "false";
            }

        } else {
            deployment = repositoryService.createDeploymentQuery().processDefinitionKey(gatewayType).singleResult();
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        Evection evection = new Evection();
        evection.setDay(day);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), ImmutableMap.of("evection", evection));
        return "流程实例ID: " + processInstance.getId();
    }


}
