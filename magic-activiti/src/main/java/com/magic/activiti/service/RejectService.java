package com.magic.activiti.service;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2025-08-02 19:26
 **/
@Service
@Slf4j
public class RejectService {

    @Autowired
    @Lazy
    private RepositoryService repositoryService;
    @Autowired
    @Lazy
    private TaskService taskService;
    @Autowired
    @Lazy
    private RuntimeService runtimeService;
    @Autowired
    @Lazy
    private HistoryService historyService;
    @Autowired
    @Lazy
    private ManagementService managementService;

    public String deployment() {
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/reject.bpmn20.xml").deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), ImmutableMap.of("initiator", "初审人", "reviewers", "复审组"));
        return processInstance.getId();
    }


    /**
     * 获取上一个节点
     *
     * @param taskId
     * @return
     */
    public String getPreviousActivityId(String taskId, Boolean pre) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (Objects.isNull(task)) {
            return "未找到对应的任务";
        }

        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .executionId(task.getExecutionId())
                .activityType(BpmnXMLConstants.ELEMENT_TASK_USER)
                .finished()
                .orderByHistoricActivityInstanceEndTime().desc().list();

        // 返回前一个节点
        if (pre) {
            return CollectionUtils.isNotEmpty(list) ? list.get(0).getActivityId() : null;
        }

        // 返回到第一个节点
        return org.springframework.util.CollectionUtils.lastElement(list).getActivityId();
    }

    /**
     * 驳回到上一个节点
     *
     * @param taskId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String rejectPre(String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (Objects.isNull(task)) {
            return "未找到对应的任务";
        }

        // 获取上一步activityId
        String previousActivityId = getPreviousActivityId(taskId,false);

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());

        // ActivityImpl: 节点定义对象， 静态定义（流程部署时生成，代表 BPMN 中一个节点的 “蓝图”）
        ActivityImpl preActivity = processDefinitionEntity.findActivity(previousActivityId);

        // execution: 驱动流程流转的载体，记录：流程当前跑到哪个节点、携带的变量、流转状态。 一个流程实例可以有一个或多个execution（并行网管）
        // ActInst：
            // 活动实例。流程里的每一个节点（开始节点、用户任务、网关、结束节点）执行一次，就生成一条 ActInst。记录流程的节点执行轨迹（流程走到哪了、走过哪些节点）。纯记录，只用来记账：哪个节点、何时开始、何时结束
            // hi_actinst中的act_id 对应的是bpmn文件中每个节点的id值（静态定义）

        managementService.executeCommand(new Command<Void>() {
            @Override
            public Void execute(CommandContext commandContext) {
                ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(task.getExecutionId());
                TaskEntity taskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);

                executionEntity.setActivity(null);

                // 激活目标节点（上一步）
                executionEntity.setActivity(preActivity);
                executionEntity.executeActivity(preActivity);

                // 使用此方式删除正在执行的任务
                commandContext.getTaskEntityManager().deleteTask(taskEntity, "驳回填写节点", false);
                return null;
            }
        });

        // 删除当前任务，终止当前节点 【直接使用此方法报错：The task cannot be deleted because is part of a running process】
        // taskService.deleteTask(taskId,true);

        // 获取当前执行流
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        // executionEntity.setActivity(null);
        // 激活目标节点（上一步）
        // executionEntity.setActivity(preActivity);
        // executionEntity.executeActivity((PvmActivity)preActivity);


        return executionEntity.getCurrentActivityId();
    }

}

