package com.comaymanagement.cmd.customentity;

import java.util.Set;

import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomEmployeeAll {
	
	private String id;
	private String name;
	private String dateOfBirth;
	private String email;
	private String phoneNumber;
	private Set<Position> positionList;
	private Set<Department> departmentList;
}
