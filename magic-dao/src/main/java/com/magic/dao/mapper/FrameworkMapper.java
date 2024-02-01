package com.magic.dao.mapper;

import com.magic.dao.model.Framework;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2024-02-01 17:32
 **/
@Mapper
public interface FrameworkMapper {

    @Select(" select * from t_framework where deleted=0")
    List<Framework> getFrameworkList();
}
