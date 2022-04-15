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
	private Integer id;
	private String code;
	private String name;
	private String dateOfBirth;
	private String email;
	private String phoneNumber;
	private Integer managerId;
	private String avatar;
	private String gender;
	private List<CustomPositionAll> positions;
	private CustomDepartmentAll department;
	private User user;
	private boolean isActive;
	private String createDate;
}
