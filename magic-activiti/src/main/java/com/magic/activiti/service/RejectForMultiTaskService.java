package com.magic.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *      多实例任务【3个审批人，全部通过时此节点才算通过】。第一个通过，第二个驳回时场景
 * @author Cheng Yufei
 * @create 2026-04-24 18:22
 **/
@Service
@Slf4j
public class RejectForMultiTaskService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;

    public String reject(String taskId) {


        Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();

        managementService.executeCommand(new Command<CommandContext>() {
            @Override
            public CommandContext execute(CommandContext commandContext) {
                TaskEntity currenttaskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);
                ExecutionEntity currentExecutionEntity = currenttaskEntity.getExecution();


                ExecutionEntity parentExecution = currentExecutionEntity.getParent();
                if (Objects.isNull(parentExecution)) {
                    return null;
                }

                ActivityImpl parentActivity = parentExecution.getActivity();

                // 用于判读是否是多实例节点类型
                ActivityBehavior parentActivityBehavior = parentActivity.getActivityBehavior();

                //获取父执行下的多个子子执行
                List<ExecutionEntity> childrenExecutions = parentExecution.getExecutions();


                //获取上一个节点
                List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
                        .activityType(BpmnXMLConstants.ELEMENT_TASK_USER).processInstanceId(currentTask.getProcessInstanceId())
                        .finished().orderByHistoricActivityInstanceEndTime().desc().list();
                String targetActivityId = activityInstances.stream().filter(a -> !a.getActivityId().equals(currentExecutionEntity.getActivityId())).map(HistoricActivityInstance::getActivityId).findFirst().orElse(null);

                // 获取ProcessDefinitionEntity，使用 repositoryService#getProcessDefinition , 下面两种注掉的方式虽然可以查出ProcessDefinitionEntity，但是其activities属性为空，导致findActivity无法找到对应的ActivityImpl
                ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(currentTask.getProcessDefinitionId());
                // ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(currentTask.getProcessDefinitionId()).singleResult();
                // ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager().findProcessDefinitionById(currentTask.getProcessDefinitionId());
                ActivityImpl targetActivity = processDefinitionEntity.findActivity(targetActivityId);

                if (CollectionUtils.isNotEmpty(childrenExecutions) && parentActivityBehavior instanceof MultiInstanceActivityBehavior) {

                    for (ExecutionEntity childrenExecution : childrenExecutions) {
                        if (childrenExecution.isEnded()) {
                            continue;
                        }
                        //删除每个子执行下的任务
                        List<TaskEntity> tasks = childrenExecution.getTasks();
                        for (TaskEntity task : tasks) {
                            commandContext.getTaskEntityManager().deleteTask(task,"多实例用户-任务驳回",false);
                            System.out.println();
                        }
                        // childrenExecution.setEnded(true); //无效，act_ru_execution表中仍然会存在多实例任务的驳回残留数据，执行最后一个节点时，删除act_ru_execution会报错。

                        // 删除子执行关联着 ACT_RU_VARIABLE 运行时变量。避免最后流程结束时删除act_ru_execution时 因为外键ACT_FK_VAR_EXE导致删除失败
                        childrenExecution.getVariableInstanceEntities().forEach((k,v)->{
                            commandContext.getVariableInstanceEntityManager().delete(v);
                            System.out.println();
                        });
                        // 删除子执行
                        commandContext.getExecutionEntityManager().delete(childrenExecution);
                    }

                    parentExecution.setActivity(null);
                    parentExecution.setActivity(targetActivity);
                    parentExecution.executeActivity(targetActivity);
                } else {
                    currentExecutionEntity.setActivity(targetActivity);
                }
                return null;
            }
        });
        return "";
    }

}

