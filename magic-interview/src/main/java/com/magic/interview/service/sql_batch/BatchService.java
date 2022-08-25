package com.magic.interview.service.sql_batch;

import com.magic.dao.mapper.UserMapper;
import com.magic.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-25 11:02
 **/
@Service
@Slf4j
public class BatchService {

    @Autowired
    private SqlSessionFactory dynamicSqlSessionFactory;
    @Autowired
    private UserMapper userMapper;

    public void insertB() {
        SqlSession sqlSession = dynamicSqlSessionFactory.openSession(ExecutorType.BATCH, false);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User u;
        long startTime = System.currentTimeMillis();
        try {
            for (int i = 1; i < 100; i++) {
                u = new User(i, "爱与痛的边缘" + i, "pwd" + i);
                userMapper.insertSelective(u);
                if (i % 20 == 0) {
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
                //出错的一批及其以后数据不会入库，之前的数据正常入库
                if (i == 22) {
                    int a = 0;
                    int i1 = 100 / a;
                }
            }
        } finally {
            sqlSession.close();
        }
        System.out.println("batch 耗时： " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void insertB2() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8", "root", "123456");
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into t_user_1 ( username, pwd ) values ( ?, ? )");

        try {
            for (int i = 1; i <= 100; i++) {
                preparedStatement.setString(1, "后视镜_" + i);
                preparedStatement.setString(2, "越来越远的_" + i);
                preparedStatement.addBatch();
                if (i % 20 == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                }
                if (i == 43) {
                    int a = 0;
                    int i1 = 100 / a;
                }
            }
        } finally {
            connection.close();
        }
    }


    public void single() {
        User u;
        long startTime = System.currentTimeMillis();
        for (int i = 512; i < 1012; i++) {
            u = new User(i, "爱与痛的边缘" + i, "pwd" + i);
            userMapper.insertSelective(u);
        }
        System.out.println("single 耗时： " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public void foreachInsert() {
        User u;
        List<User> users = new ArrayList<>();
        for (int i = 1013; i < 1513; i++) {
            u = new User(i, "爱与痛的边缘" + i, "pwd" + i);
            users.add(u);
        }
        long startTime = System.currentTimeMillis();
        userMapper.insertList(users);
        System.out.println("foreach 耗时： " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
