package com.magic.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrameworkVo3 {

	@JsonProperty("name")
	private String name = "一首歌的时间";

	@JsonProperty("frameworkId")
	private long frameworkId;

	@JsonProperty("parentId")
	private Long parentId;

	@JsonProperty("frameworkName")
	private String frameworkName;

	@JsonProperty("treeName")
	private String treeName;
	// @JsonBackReference  ： 解决无限递归问题，标注的属性在序列化时会被忽略
	private List<FrameworkVo3> childrenList;

	@JsonProperty("aliasNameList")
	private List<String> aliasNameList;
}
