package com.magic.time.service.business_development_100.pool_04;

import com.magic.time.dao.mapper.UserInfoMapper;
import com.magic.time.dao.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-18 4:42 PM
 **/
@Service
@Slf4j
public class DatabaseService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfo getUser(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    /**
     * 测试数据库连接池使用情况。一个 @Transactional 占用一个数据库连接
     * @return
     */
    @Transactional
    public UserInfo insertUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("测试占用连接");
        userInfoMapper.insertSelective(userInfo);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public UserInfo insertUser(String username, MultipartFile file) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfoMapper.insertSelective(userInfo);

        FileUtils.copyToFile(file.getInputStream(),new File("/Users/chengyufei/Downloads/project/B.png"));
        return userInfo;
    }

}
