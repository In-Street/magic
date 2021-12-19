package com.magic.time.service.business_development_100.pool_04;

import com.magic.time.dao.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getUser")
    public UserInfo getUser(@RequestParam Integer id) {
        return databaseService.getUser(id);
    }

    @GetMapping("/insertUser")
    public UserInfo insertUser() {
        return databaseService.insertUser();
    }
}
