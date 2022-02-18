package com.magic.time.dao.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author 
 * 
 */
@Data
public class UserInfo implements Serializable {
    private Integer id;

    private String username;

    private String avatar;

    private String address;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public UserInfo() {
    }

    public UserInfo(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}