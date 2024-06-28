package com.magic.base.dto;

import com.magic.dao.model.Framework;
import lombok.*;

import java.util.List;

@With
@Builder
@ToString
public class FrameworkVo2 {
	private String name;
	private long frameworkId;
	private Long parentId;
	private String frameworkName;
	private String treeName;
	// @JsonBackReference  ： 解决无限递归问题，标注的属性在序列化时会被忽略
	private List<FrameworkVo2> childrenList;

	@Singular("S1")
	private List<String> aliasNameList;
}
