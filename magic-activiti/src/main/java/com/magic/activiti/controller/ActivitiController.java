package com.magic.activiti.controller;

import com.magic.activiti.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Cheng Yufei
 * @create 2024-02-14 18:32
 **/
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;
    @GetMapping("/deploy")
    public String deploy(){
        return activitiService.deploy();
    }

    @GetMapping("/deployByZip")
    public String deployByZip() throws IOException {
        return activitiService.deployByZip();
    }

    @GetMapping("/startProcess")
    public String startProcess(){
        return activitiService.startProcess();
    }

    @GetMapping("/getTaskByAssignee")
    public String getTaskByAssignee(String assignee){
        return activitiService.getTaskByAssignee(assignee);
    }

    @GetMapping("/completeTask")
    public String completeTask(String taskId){
        return activitiService.completeTask(taskId);
    }

    @GetMapping("/deleteDeploymentById")
    public String deleteDeploymentById(String  deploymentId){
        return activitiService.deleteDeploymentById(deploymentId);
    }

    @GetMapping("/getResource")
    public String getResource(String  deploymentId){
        return activitiService.getResource(deploymentId);
    }

}
