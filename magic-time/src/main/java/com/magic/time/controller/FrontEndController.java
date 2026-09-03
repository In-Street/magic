package com.magic.time.controller;

import com.magic.base.dto.Result;
import com.magic.time.dtos.StudentQueryReq;
import com.magic.time.dtos.StudentResp;
import com.magic.time.service.front_end.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2026-07-01 11:36
 **/
@RestController
@RequestMapping("/frontEnd")
public class FrontEndController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getAllStudent")
    public Result getAllStudent() {
        List<StudentResp> resps = studentService.getAllStudent();
        // return Result.success(Collections.emptyList());
        return Result.success(resps);
        // return Result.fail(20001,"不可访问");
    }

    @GetMapping("/getStudentList")
    public Result getStudentList(StudentQueryReq req) {
        return studentService.getStudentList(req);
    }

    @PostMapping("/delStudentById")
    public Result delStudentById(@RequestParam List<Integer> ids) {
        return studentService.deleteUserById(ids);
    }
    
    @PostMapping("/saveUser")
    public Result saveUser(@RequestBody StudentResp request){
        return Result.success(studentService.saveUser(request));
    }
}
