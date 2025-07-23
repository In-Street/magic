package com.magic.activiti.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * global-evection 流程变量
 * @author Cheng Yufei
 * @create 2025-07-21 12:08
 **/
@Getter
@Setter
public class Evection implements Serializable {


    private static final long serialVersionUID = 7828558841621662248L;

    private Integer id;

    /**
     * 请假天数
     */
    private Double day;

    /**
     * 请假开始、结束时间
     */
    private Date beginTime;
    private Date endTime;

    /**
     * 目的地
     */
    private String destination;

}
