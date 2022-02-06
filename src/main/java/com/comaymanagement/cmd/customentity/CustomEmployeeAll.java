package com.comaymanagement.cmd.customentity;

import java.util.List;
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
	private Integer unique_number;
	private String id;
	private String name;
	private String avatar;
	private String gender;
	private String dateOfBirth;
	private String email;
	private String phoneNumber;
	private List<CustomPositionAll> positionList;
	private CustomDepartmentAll department;
	private User user;
}
