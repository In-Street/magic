package com.magic.time.dtos;

import lombok.Data;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2026-07-14 16:12
 **/
@Data
public class StudentQueryReq {

    private String name;
    private Integer gender;
    private List<Integer> age;

    private Integer ageMin;
    private Integer ageMax;

    private Integer pageNum;
    private Integer pageSize;
}
