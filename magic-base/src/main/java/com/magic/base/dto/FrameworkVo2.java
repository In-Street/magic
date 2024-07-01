package com.magic.base.dto;

import lombok.*;

import java.util.List;

@With
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FrameworkVo2 {
	@Builder.Default
	private String name = "一首歌的时间";
	private long frameworkId;
	private Long parentId;
	private String frameworkName;
	private String treeName;
	// @JsonBackReference  ： 解决无限递归问题，标注的属性在序列化时会被忽略
	private List<FrameworkVo2> childrenList;

	@Singular("S1")
	private List<String> aliasNameList;
}
