package com.magic.base.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.magic.dao.model.Framework;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
public class FrameworkVo {
	private String name;
	private long frameworkId;
	private Long parentId;
	private String frameworkName;
	private String treeName;
	// @JsonBackReference  ： 解决无限递归问题，标注的属性在序列化时会被忽略
	private List<FrameworkVo> childrenList;
}
