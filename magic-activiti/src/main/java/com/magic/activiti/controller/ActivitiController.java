package com.magic.activiti.controller;

import com.magic.activiti.service.ActivitiService;
import com.magic.activiti.service.AssigneeService;
import com.magic.activiti.service.CandidateService;
import com.magic.activiti.service.GlobalVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private AssigneeService assigneeService;
    @Autowired
    private GlobalVariableService globalVariableService;
    @Autowired
    private CandidateService candidateService;
    
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
    public String getTaskByAssignee(String assignee,String procDefKey){
        return activitiService.getTaskByAssignee(assignee,procDefKey);
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


    @GetMapping("/setUpBusinessKey")
    public String setUpBusinessKey(){
        return activitiService.setUpBusinessKey();
    }

    /**
     * 流程定义被挂起 、激活
     * @return
     */
    @GetMapping("/suspendAndActivate")
    public String suspendAndActivate (){
        return activitiService.suspendAndActivate();
    }


        @GetMapping("/deployment")
        public String deployment(){
            // return assigneeService.uelAssigneeProcDef();
            // return globalVariableService.deploymentGlobal();
            return candidateService.candidateDeployment();
        }
        
        @GetMapping("/startProcInst")
        public String startProcInst(@RequestParam(required = false)String deploymentId,@RequestParam(required = false) Double day,@RequestParam(required = false) String candidateUser){
            // return assigneeService.uelAssigneeProcInst(deploymentId);
            // return globalVariableService.startProcessInstanceGlobal(deploymentId, day);
            return candidateService.searchGroupTask(candidateUser);
        }
    @GetMapping("/startProcInstForListener")
    public String startProcInstForListener(String deploymentId){
        return assigneeService.listenerAssigneeProcInst(deploymentId);
    }

    @GetMapping("/claimTask")
    public String claimTask(String taskId,String user){
        return candidateService.claimTask(taskId, user);
    }

    @GetMapping("/returnTask")
    public String returnTask(String taskId,String user){
        return candidateService.returnTask(taskId, user);
    }

    @GetMapping("/transferTask")
    public String transferTask(String taskId,String assign,String user){
        return candidateService.transferTask(taskId, assign,user);
    }

}
