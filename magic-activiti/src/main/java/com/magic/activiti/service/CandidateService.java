package com.magic.activiti.service;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Cheng Yufei
 * @create 2025-07-23 11:50
 **/
@Service
@Slf4j
public class CandidateService {

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
    private IdentityService identityService;

    public String candidateDeployment() {
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("bpmn/candidate-evection.bpmn20.xml").addClasspathResource("bpmn/candidate-evection.png").deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());

        return processInstance.getId();
    }

    /**
     * 根据候选人查询组任务
     *
     * @param candidateUser
     * @return
     */
    public String searchGroupTask(String candidateUser) {

        List<Task> task = taskService.createTaskQuery().taskCandidateUser(candidateUser).list();
        StringBuilder stringBuilder = new StringBuilder();
        for (Task t : task) {
            stringBuilder.append("任务ID：").append(t.getId()).append(System.lineSeparator())
                    .append("任务负责人：").append(t.getAssignee()).append(System.lineSeparator())
                    .append("===========");
        }
        return stringBuilder.toString();
    }


    /**
     * 认领任务
     *
     * @param taskId
     * @param user
     * @return
     */
    public String claimTask(String taskId, String user) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String oldAssignee = task.getAssignee();

        // 获取该任务下的所有候选人，判断候选人是否存在
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        List<IdentityLink> candidateList = identityLinksForTask.stream().filter(i -> StringUtils.equals(IdentityLinkType.CANDIDATE, i.getType())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(candidateList)) {
            return "该任务下不存在 " + user + " 候选人";
        }
        if (candidateList.stream().noneMatch(c-> StringUtils.equals(user,c.getUserId()))) {
            return "该任务下不存在 " + user + " 候选人";
        }

        taskService.claim(taskId, user);

        Task taskNew = taskService.createTaskQuery().taskId(taskId).singleResult();
        String newAssignee = taskNew.getAssignee();

        return oldAssignee + "==== " + newAssignee;
    }

    /**
     * 归还任务:  将任务负责人设置为null
     *
     * @return
     */
    public String returnTask(String taskId, String assignee) {

        Task task = taskService.createTaskQuery().taskId(taskId).taskAssignee(assignee).singleResult();
        if (Objects.isNull(task)) {
            return assignee+" 名下无此任务";
        }
        taskService.setAssignee(taskId, null);

        Task taskNew = taskService.createTaskQuery().taskId(taskId).singleResult();
        return taskNew.getAssignee();
    }

    /**
     * 交接任务
     *
     * @return
     */
    public String transferTask(String taskId, String assignee,String user) {

        Task task = taskService.createTaskQuery().taskId(taskId).taskAssignee(assignee).singleResult();
        if (Objects.isNull(task)) {
            return assignee+" 名下无此任务";
        }
        taskService.setAssignee(taskId, user);

        Task taskNew = taskService.createTaskQuery().taskId(taskId).singleResult();
        return taskNew.getAssignee();
    }
}
