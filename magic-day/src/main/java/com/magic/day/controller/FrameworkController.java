package com.magic.day.controller;

import com.magic.base.dto.FrameworkVo;
import com.magic.day.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2024-02-01 17:35
 **/
@RestController
@RequestMapping("/frame")
public class FrameworkController {

    @Autowired
    private FrameworkService frameworkService;

    /**
     *  Thrones / Fragment / 2/7
     * @return
     */
    @GetMapping("/getFrameworkTree")
    public List<FrameworkVo> getFrameworkTree(){
        return frameworkService.listFrameworkTree();
    }
}
