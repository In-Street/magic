package com.magic.time.service.front_end;

import com.magic.base.dto.Result;
import com.magic.time.dao.mapper.UserInfoMapper;
import com.magic.time.dao.mapper.UserMapper;
import com.magic.time.dao.model.User;
import com.magic.time.dao.model.UserExample;
import com.magic.time.dao.model.UserInfo;
import com.magic.time.dao.model.UserInfoExample;
import com.magic.time.dtos.StudentQueryReq;
import com.magic.time.dtos.StudentResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2026-07-01 11:37
 **/
@Service
@Slf4j
public class StudentService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;

    public List<StudentResp> getAllStudent() {
        UserInfoExample userInfoExample = new UserInfoExample();
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        List<StudentResp> studentRespList = new ArrayList<>();
        userInfos.forEach(userInfo -> {
            StudentResp studentResp = new StudentResp();
            BeanUtils.copyProperties(userInfo, studentResp);
            studentResp.setName(userInfo.getUsername());
            studentRespList.add(studentResp);
        });
        return studentRespList;
    }

    public Result getStudentList(StudentQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (StringUtils.isNoneBlank(req.getName())) {
            criteria.andUsernameLike("%"+req.getName()+"%");
        }
        if (Objects.nonNull(req.getGender()) && req.getGender() > 0) {
            criteria.andGenderEqualTo(req.getGender());
        }
        /*if (CollectionUtils.isNotEmpty(req.getAge())) {
            criteria.andAgeGreaterThanOrEqualTo(req.getAge().get(0))
                    .andAgeLessThanOrEqualTo(req.getAge().get(1));
        }*/

        if (Objects.nonNull(req.getAgeMin()) && req.getAgeMin() > 0) {
            criteria.andAgeGreaterThanOrEqualTo(req.getAgeMin());
        }

        if (Objects.nonNull(req.getAgeMax()) && req.getAgeMax() > 0) {
            criteria.andAgeLessThanOrEqualTo(req.getAgeMax());
        }

        userExample.setOffset(((long) (req.getPageNum() - 1) * req.getPageSize()));
        userExample.setLimit(req.getPageSize());
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return Result.success();
        }
        long total = userMapper.countByExample(userExample);

        List<StudentResp> studentRespList = new ArrayList<>();
        users.forEach(userInfo -> {
            StudentResp studentResp = new StudentResp();
            BeanUtils.copyProperties(userInfo, studentResp);
            studentResp.setName(userInfo.getUsername());
            studentRespList.add(studentResp);
        });
        Result result = Result.success(studentRespList);
        result.setTotal((int) total);
        return result;
    }

    public Result deleteUserById(List<Integer> ids) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(ids);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return Result.fail(10001, "数据不存在");
        }

        userMapper.deleteByExample(userExample);
        return Result.success();
    }

    public int saveUser(StudentResp request) {

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(request.getName());
        userInfo.setAge(request.getAge());
        userInfo.setGender(request.getGender());

        return userInfoMapper.insertSelective(userInfo);

    }
}
