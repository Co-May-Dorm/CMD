package com.comaymanagement.cmd.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class TeamModel{
	private Integer id;
	private String code;
	private String name;
	private Integer headPosition;
	private List<PositionModel> positions;
	private String description;
	private List<EmployeeModel> employees;
	private Integer level;
	private PositionModel position;
}
