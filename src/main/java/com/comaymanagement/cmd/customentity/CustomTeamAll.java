package com.comaymanagement.cmd.customentity;

import java.util.List;

import com.comaymanagement.cmd.entity.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTeamAll{
	private Integer id;
	private String code;
	private String name;
	private Integer headPosition;
	private List<CustomPositionAll> positions;
	private String description;
	private List<CustomEmployeeAll> employees;
	private Integer level;
}
