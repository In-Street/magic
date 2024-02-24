package com.magic.activiti.controller;

import com.magic.activiti.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.printf("出差申请流程部署，id:%s，测试合并分支部分文件","AAAA");
        return activitiService.deploy();
    }
}
