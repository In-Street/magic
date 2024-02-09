package com.magic.base.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.util.List;

@Data
public class FrameworkVo {
	private String name;
	private long frameworkId;
	private Long parentId;
	private String frameworkName;
	private String treeName;
	// @JsonBackReference
	private List<FrameworkVo> childrenList;
}
