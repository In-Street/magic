package com.magic.time.dtos;

import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2026-07-01 11:47
 **/
@Setter
public class StudentResp implements Serializable {


    private static final long serialVersionUID = -1223888980955518174L;

    private Integer id;
    private String name;
    private Integer age;
    private Integer gender;
    private String genderStr;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getGender() {
        return gender;
    }

    public String getGenderStr() {
        return Objects.equals(this.gender, 1) ? "男" : "女";
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = Objects.equals(this.gender, 1) ? "男" : "女";
    }
}
