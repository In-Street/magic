package com.magic.day.service;

import com.magic.base.dto.FrameworkVo;
import com.magic.dao.mapper.FrameworkMapper;
import com.magic.dao.model.Framework;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Cheng Yufei
 * @create 2024-02-01 17:42
 **/
@Service
@Slf4j
public class FrameworkService {

    @Autowired
    private FrameworkMapper frameworkMapper;
    public List<FrameworkVo> listFrameworkTree() {

        List<Framework> list=frameworkMapper.getFrameworkList();

        List<FrameworkVo> voList = list.stream().map(f -> {
            FrameworkVo vo = new FrameworkVo();
            vo.setFrameworkId(f.getFrameworkId());
            vo.setName(f.getFrameworkName());
            vo.setParentId(f.getNewParentId());
            vo.setTreeName(f.getTreeName());
            return vo;
        }).collect(Collectors.toList());

        Map<Long, List<FrameworkVo>> parentIdMap = voList.stream().parallel().collect(Collectors.groupingBy(FrameworkVo::getParentId));

        voList.stream().forEach(v->v.setChildrenList(parentIdMap.get(v.getFrameworkId())));

        List<FrameworkVo> res = voList.stream().filter(v -> v.getParentId() == 0L).collect(Collectors.toList());

        return res;
    }
}
