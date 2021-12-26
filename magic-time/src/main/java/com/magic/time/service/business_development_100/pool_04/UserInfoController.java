package com.magic.time.service.business_development_100.pool_04;

import com.magic.time.dao.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-18 5:45 PM
 **/
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private HttpSerivce httpSerivce;

    @GetMapping("/getUser")
    public UserInfo getUser(@RequestParam Integer id) {
        return databaseService.getUser(id);
    }

    @GetMapping("/insertUser")
    public UserInfo insertUser() {
        return databaseService.insertUser();
    }

    @PostMapping("/insertUser2")
    public String insertUser2(MultipartFile file, String username) throws IOException {
        databaseService.insertUser(username, file);
        return "success";
    }

    @GetMapping("/rest")
    public void rest() throws URISyntaxException {
        httpSerivce.rest1();
    }

    @PostMapping("/rest2")
    public void rest2() {
        httpSerivce.rest2();
    }

    @PostMapping("/rest3")
    public void rest3(MultipartFile fileParam, String username) {
        httpSerivce.rest3(fileParam, username);
    }
}
