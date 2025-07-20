package com.magic.activiti.service;


import cn.anony.annotations.ElementVersion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.xml.ws.Response;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author Cheng Yufei
 * @create 2024-02-14 18:35
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ ={ @Autowired,@Lazy})
public class ActivitiService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;


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
     *  将多个bpmn打zip包，一次部署多个流程
     * @return
     * @throws IOException
     */
    public String deployByZip() throws IOException {

        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        InputStream inputStream = patternResolver.getResource("bpmn/Aevection.zip").getInputStream();

        // InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("bpmn/Aevection.zip");

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        log.info("通过zip包进行流程部署，id:{}",deploy.getId());
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
     *
     *  startProcessInstanceByKey(processDefinitionKey,businessKey): 将业务ID绑定到activiti，act_ru_execution
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
        log.info("流程部署ID：{}",myEvectionProcessInstance.getDeploymentId());

        return "success";
    }

    /**
     * 查询个人待执行的任务：
     *     select distinct RES.*
     *   from ACT_RU_TASK RES
     *          inner join ACT_RE_PROCDEF D on RES.PROC_DEF_ID_ = D.ID_
     * WHERE RES.ASSIGNEE_ = 'zhangsan'
     *   and D.KEY_ = 'myEvection'
     * order by RES.ID_ asc
     * LIMIT 2147483647 OFFSET 0
     *
     *
     * @param assignee
     * @return
     */
    public String getTaskByAssignee(String assignee,String procDefKey) {

        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(procDefKey) // 流程Key
                // .taskAssignee(assignee) // 要查询的负责人
                .list();

        for (Task task : taskList) {
            log.info("流程实例ID：{}", task.getProcessInstanceId());
            log.info("任务ID：{}", task.getId());
            log.info("任务负责人：{}", task.getAssignee());
            log.info("任务名称：{}", task.getName());
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            String deploymentId = processDefinition.getDeploymentId();
            log.info("流程部署ID：{}", deploymentId);
        }
        return "success";
    }

    /**
     * 完成个人任务
     * @param taskId
     * @return
     */
    public String completeTask(String taskId) {
        taskService.complete(taskId);
        // taskService.delegateTask(); //委托任务给其它人
        //TODO BY Cheng Yufei <-2024-05-04 20:18->
        //  如何驳回？
        return "success";
    }

    /**
     *  删除流程部署，不会删除已完成任务的hi 相关表数据
     *
     *   delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ =
     *   delete from ACT_RE_PROCDEF where DEPLOYMENT_ID_ = ，若 act_ru_execution 中有关联的数据，会删除失败，因为两者有外键约束。 若部署ID对应的流程节点均已完成，则可以完成删除。有正在进行中的任务是无法deleteDeployment()方法删除的
     *   delete from ACT_GE_BYTEARRAY where DEPLOYMENT_ID_ =
     *   delete from ACT_RE_DEPLOYMENT where ID_ = ?
     *
     *   有在进行中任务时，可采用 deleteDeployment(deploymentId,true) 级联删除的方式进行流程部署的删除，同时会删除 ru 和 hi 相关表数据；
     *
     * @param deploymentId
     * @return
     */
    public String deleteDeploymentById(String deploymentId) {
        // repositoryService.deleteDeployment(deploymentId);
        repositoryService.deleteDeployment(deploymentId,true);
        return "success";
    }

    /**
     * 流程资源文件下载：
     *
     *
     * @param deploymentId
     * @return
     */
    public String getResource(String deploymentId)  {

        // 1. 获取资源文件名.  ACT_RE_PROCDEF 表中存有 DEPLOYMENT_ID_ 对应的资源名
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        String bpmnName = processDefinition.getResourceName();
        String pngName = processDefinition.getDiagramResourceName();

        String result = StringUtils.EMPTY;
        try (
                // 2.  根据流程部署ID+资源名，获取存在库表【ACT_GE_BYTEARRAY】里的资源
                InputStream bpmnInputStream = repositoryService.getResourceAsStream(deploymentId, bpmnName);
                InputStream pngInputStream = repositoryService.getResourceAsStream(deploymentId, pngName);

                //3. 构建输出流
                OutputStream pngOutputStream = Files.newOutputStream(new File("/Users/chengyufei/Downloads/project/self/evection.png").toPath());
                FileOutputStream bpmnOutputStream = new FileOutputStream(new File("/Users/chengyufei/Downloads/project/self/evection.bpmn20.xml"));
        ) {

            //4. 输入流 输出流转换
            int pngResult = StreamUtils.copy(pngInputStream, pngOutputStream);
            int bpmnResult = StreamUtils.copy(bpmnInputStream, bpmnOutputStream);
            result = String.valueOf(pngResult) + String.valueOf(bpmnResult);

        } catch (IOException e) {

        }
        return result;

    }

    public String getHistory(String processInstanceId){

        // historyService.createHistoricTaskInstanceQuery()

        // 获取 ACT_HI_ACTINST 表查询对象
        HistoricActivityInstanceQuery activityInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId);
        activityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();

        List<HistoricActivityInstance> list = activityInstanceQuery.list();

        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityName()+"：开始于 ");
        }

        return "success";
    }

    /**
     *  流程实例启动时，关联 业务ID【businessKey】
     *
     *   ACT_RU_EXECUTION 、ACT_HI_PROCINST  两张表中均存有 businessKey
     * @return
     */
    public String setUpBusinessKey(){

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("myEvection").singleResult();

        String businessKey = "123-456";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), businessKey);
        return processInstance.getId();

    }

    /**
     * 流程定义被挂起、激活
     *  场景：月底不处理出差审批，但有人提交了申请。该申请被挂起后，流程不能被继续处理，只有被激活后才能处理
     *
     * @return
     */
    public String suspendAndActivate(){

        ProcessDefinition myEvectionDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();

        // 流程定义挂起
        repositoryService.suspendProcessDefinitionById(myEvectionDefinition.getId(),true,new Date());

        boolean processDefinitionSuspended = repositoryService.isProcessDefinitionSuspended(myEvectionDefinition.getId());
        log.info(myEvectionDefinition.getName()+"流程定义是否被挂起：{}",processDefinitionSuspended);

        //流程实例挂起、激活
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.suspendProcessInstanceById(processInstance.getId());
            runtimeService.activateProcessInstanceById(processInstance.getId());

        }
        return "success";
    }
}
