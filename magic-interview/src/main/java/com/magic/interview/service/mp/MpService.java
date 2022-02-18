package com.magic.interview.service.mp;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import com.magic.dao.model.UserExample;
import com.magic.dao.mp.UserMapperMp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-16 2:17 PM
 **/
@Service
@Slf4j
public class MpService {

    @Autowired
    private UserMapperMp userMapperMp;
    @Autowired
    private UserMapper userMapper;


    public List<User> list() {
        List<User> users = userMapperMp.selectList(null);

        User user = new User();
        user.setUsername("夜的第七章");
        user.setPubPlatform();
        return users;
    }

    public List<User> list2() {
        UserExample example = new UserExample();
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

}
