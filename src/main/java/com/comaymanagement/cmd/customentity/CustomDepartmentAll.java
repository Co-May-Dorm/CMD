package com.comaymanagement.cmd.customentity;

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
	private String id;
	
	private String name;
	private String fatherDepartmentId;
	private String managerId;
	
	private Set<Position> positionList;
	
}
