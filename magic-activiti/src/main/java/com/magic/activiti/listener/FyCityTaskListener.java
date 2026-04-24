package com.magic.activiti.listener;

import com.magic.activiti.config.Declare;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

/**
 *   市级审批人分配：
 *      只处理由县级审批完成后流转过来的分配。 不处理 申报-> 市级审批 的情况，此情况有初始化首个审批节点的FyTaskListener完成
 *
 * @author Cheng Yufei
 * @create 2026-04-23 11:58
 **/
@Component
public class FyCityTaskListener implements TaskListener {


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
        Integer level = declare.getLevel(); // 申报级别：  1 县级  2市级  3省级
        if (level > 1) { // 只处理县级流转过来的。 市级申报的由初始化的监听器进行分配
            return;
        }

        String areaId = declare.getAreaId(); // 申报辖区编码  eg： 140101 、1401、14
        String assignee = StringUtils.substring(areaId, 2, 4) + "-流转市级审批人";
        delegateTask.setAssignee(assignee);

    }
}
