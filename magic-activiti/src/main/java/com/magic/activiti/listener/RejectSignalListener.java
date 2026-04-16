package com.magic.activiti.listener;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 回退信号监听器
 *
 * @author Cheng Yufei
 * @create 2025-08-03 18:15
 **/
public class RejectSignalListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {

        // 回退原因
        String rejectReason = (String) execution.getVariable("rejectReason");

        //初审节点完成时的责任人
        String taskAAssignee = (String) execution.getVariable("taskA_assignee");

        // 重置初审节点的责任人
        execution.setVariable("initiator",taskAAssignee);

        // 记录回退日志
        TaskService taskService = execution.getEngineServices().getTaskService();
        taskService.addComment(null, execution.getProcessInstanceId(), "驳回到初审：" + rejectReason);

        // 清除回退标记
        execution.removeVariable("reject");
    }
}
