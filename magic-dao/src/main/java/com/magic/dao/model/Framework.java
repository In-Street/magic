package com.magic.dao.model;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Framework  {

	private long frameworkId;
	private String frameworkName;
	private String alignName;
	private Long parentId;
	private String tree;
	private String treeName;
	private int orderId;
	private int cityTag;
	private int department;//0:非部门 1:设计部门 2:客服部门 3:工程部 4:交付中心 5:工长组 6:人力行政7:技术部 8:产品部 9:SEM 10:站内 11:新媒体 12:VR+供应商 13:用户运营 14自营部门
	private String fullName;
	private int level;//0:未设置 1:组 2：部门 3：城市
	private int deleted;//隐藏1.是0.否
	private String shortName;
	private Integer sort;
	private String cityName;

	private Long newParentId; //新的父级
	private String newTree; //新的tree


	// 冗余字段 - 北京拆分->北京大宅/北京套餐
	/** 虚拟区域ID */
	private Long virtualAreaId;
	/** 虚拟区域名称 */
	private String virtualAreaName;
	/**默认父级 */
	private Integer defaultId;

	private Integer frameworkType;
}
