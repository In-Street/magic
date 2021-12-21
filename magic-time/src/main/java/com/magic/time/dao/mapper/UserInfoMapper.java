package com.magic.time.dao.mapper;

import com.magic.time.dao.model.UserInfo;
import com.magic.time.dao.model.UserInfoExample;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserInfoMapper继承基类
 */
@Mapper
public interface UserInfoMapper extends MyBatisBaseDao<UserInfo, Integer, UserInfoExample> {
}