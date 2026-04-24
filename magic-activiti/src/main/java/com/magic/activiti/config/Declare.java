package com.magic.activiti.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author Cheng Yufei
 * @create 2026-04-23 12:02
 **/
@Getter
@Setter
public class Declare implements Serializable {



    private static final long serialVersionUID = 4384742503220420030L;

    private Integer level;

    private String areaId;
}
