package com.magic.activiti.listener;

import com.magic.activiti.config.Declare;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cheng Yufei
 * @create 2026-04-23 11:58
 **/
@Component
public class FyProvinceTaskListener implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {

        if (!(StringUtils.equals(TaskListener.EVENTNAME_CREATE, delegateTask.getEventName()))) {
            return;
        }

        String source = delegateTask.getVariable("source", String.class);
        if (StringUtils.isBlank(source)) {
            return;
        }

        Declare declare = delegateTask.getVariable("declare", Declare.class);

        String areaId = declare.getAreaId(); // 申报辖区编码  eg： 140101 、1401、14
        String assignee = StringUtils.left(areaId, 2) + "-流转省级审批人";
        delegateTask.setAssignee(assignee);

    }
}
