package com.magic.interview.service.mp;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-16 3:03 PM
 **/
@RestController
@RequestMapping("/mp")
@RequiredArgsConstructor
public class MpController {

    private final MpService mpService;

    @GetMapping("/list")
    public List list(){
        return mpService.list();
    }

    @GetMapping("/list2")
    public List list2(){
        return mpService.list2();
    }
}
