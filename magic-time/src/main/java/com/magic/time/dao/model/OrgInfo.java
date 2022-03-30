package com.magic.time.dao.model;

import lombok.Data;

import java.lang.ref.WeakReference;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-12 5:07 PM
 **/
@Data
public class OrgInfo {

    private String orgId;

    private String orgName;

    private WeakReference<UserInfo> weakUserInfo;
    private UserInfo userInfo;
}
