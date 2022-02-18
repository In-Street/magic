package com.magic.dao.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
@TableName(value = "t_user_1")
public class User implements Serializable {
    private Integer id;

    /**
     * 版权来源
     */
    private String username;

    private String pwd;

    /**
     * 发布来源
     */
    private String pubPlatform;

    /**
     * 简介摘要
     */
    private String summary;

    private static final long serialVersionUID = 1L;

    public User(Integer id, String username, String pwd) {
        this.id = id;
        this.username = username;
        this.pwd = pwd;
    }

    public User() {
    }
}