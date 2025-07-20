package com.magic.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

/**
 * 任务监听器，当User Task 创建时，回调到notify方法指定 assignee 责任人
 * @author Cheng Yufei
 * @create 2025-07-13 19:12
 **/
public class TaskListenerConfig implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {

        // 当事件类型是 create
        if (  !(StringUtils.equals(TaskListener.EVENTNAME_CREATE, delegateTask.getEventName()))
        ) {
            return;
        }

        // “创建申请” 任务被创建时，指定责任人
        if( StringUtils.equals("创建申请", delegateTask.getName()) && StringUtils.equals("sid-42732e48-27fd-4237-9bdb-55a6f2298785",delegateTask.getTaskDefinitionKey())){
            delegateTask.setAssignee("监听器申请人");
        }

        // “审批” 任务被创建时，指定责任人
        if( StringUtils.equals("审批", delegateTask.getName()) && StringUtils.equals("sid-ee721970-6734-43d4-b66c-e499e12a9c9d",delegateTask.getTaskDefinitionKey()) ){
            delegateTask.setAssignee("监听器审批人");
        }
    }
}
