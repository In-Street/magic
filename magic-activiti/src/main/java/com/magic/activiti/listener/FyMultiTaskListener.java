package com.magic.activiti.listener;

import com.magic.activiti.config.Declare;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *   申报完成时，根据辖区初始化首个审批节点的责任人
 * @author Cheng Yufei
 * @create 2026-04-23 11:58
 **/
@Component
public class FyMultiTaskListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        if (!(StringUtils.equals(TaskListener.EVENTNAME_CREATE, delegateTask.getEventName()))) {
            return;
        }

        Declare declare = delegateTask.getVariable("declare", Declare.class);

        Integer level = declare.getLevel(); // 申报级别：  1 县级  2市级  3省级
        String areaId = declare.getAreaId(); // 申报辖区编码  eg： 140101 、1401、14
        String right_areaId = StringUtils.right(areaId, 2);

        String assignee = delegateTask.getVariable("assignee", String.class);

        switch (level) {
            case 1: // 县级
                /*if ("140101".equals(areaId)) {
                    delegateTask.setAssignee("01县级审批");
                }*/
                delegateTask.setAssignee(assignee);
                break;
            case 2: // 市级
                delegateTask.setAssignee(right_areaId + "市级审批");
                break;

            case 3: // 省级
                delegateTask.setAssignee(right_areaId + "专家审批");
                break;

        }

    }
}
