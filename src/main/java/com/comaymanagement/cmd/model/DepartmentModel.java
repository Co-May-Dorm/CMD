package com.comaymanagement.cmd.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentModel {
	private Integer id;
	private String code;
	private String name;
	private Integer fatherDepartmentId;
	private Integer headPosition;
	private List<PositionModel> positions;
	private String description;
	private List<EmployeeModel> employees;
	private Integer level;
}
