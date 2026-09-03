package com.magic.activiti.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.magic.activiti.config.Declare;
import com.magic.activiti.config.Evection;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2026-04-23 17:52
 **/
@Service
@Slf4j
public class FyService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    public String deploymentAndStartProc(Integer level, String areaId) {

        // Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/fy.bpmn20.xml").deploy();
        // ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("fy:4:26f46d78-3f07-11f1-9055-420c35f2ec3b").singleResult();

        // 设置全局变量，指定当前申报的 辖区ID和级别。 级别在排他网管中确定流程节点，根据级别在TaskListener中指定首个审批节点的责任人
        Declare declare = new Declare();
        declare.setLevel(level); // 1 县 2市 3省
        declare.setAreaId(areaId);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), ImmutableMap.of("declare", declare));
        return "流程实例ID: " + processInstance.getId();
    }

    /**
     * fy-multi.bpmn20.xml  并行会签【多实例用户任务】：
     * <userTask>
     * ...
     * <multiInstanceLoopCharacteristics isSequential="false" activiti:collection="approverList" activiti:elementVariable="assignee">
     * <completionCondition>
     * ${nrOfCompletedInstances == nrOfInstances}
     * </completionCondition>
     * </multiInstanceLoopCharacteristics>
     * <userTask/>
     *
     * @param level
     * @param areaId
     * @return
     */
    public String deploymentAndStartProcMulti(Integer level, String areaId) {

        // Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/fy-multi.bpmn20.xml").deploy();
        // ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("fy-multi:1:1307c7bb-3fc5-11f1-b7ba-eef847c5558b").singleResult();

        // 设置全局变量，指定当前申报的 辖区ID和级别。 级别在排他网管中确定流程节点，根据级别在TaskListener中指定首个审批节点的责任人
        Declare declare = new Declare();
        declare.setLevel(level); // 1 县 2市 3省
        declare.setAreaId(areaId);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), ImmutableMap.of("declare", declare));
        return "流程实例ID: " + processInstance.getId();
    }

    public String completeMultiInstanceTask(String taskId, String source) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (Objects.isNull(task)) {
            return "fail";
        }
        if (StringUtils.isNoneBlank(source)) {
            taskService.complete(taskId, ImmutableMap.of("source", "flow"));
            return "success";
        }

        // 完成申报任务时，指定 县级审批的所有审核人员，用户会签
        // 生成3个 task、execution、act_inst
        taskService.complete(taskId, ImmutableMap.of("approverList", Lists.newArrayList("县级审批员G", "县级审批员H", "县级审批员I")));
        return "success";
    }

}
