package com.comaymanagement.cmd.customentity;

import java.util.List;
import java.util.Set;

import com.comaymanagement.cmd.entity.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomDepartmentAll {
	private Integer id;
	private String code;
	private String name;
	private String fatherDepartmentId;
	private String managerId;
	private List<CustomPositionAll> positionList;
	private String description;
}
