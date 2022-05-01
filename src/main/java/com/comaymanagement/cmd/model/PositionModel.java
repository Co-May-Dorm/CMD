package com.comaymanagement.cmd.model;

import com.comaymanagement.cmd.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PositionModel {
	private Integer id;
	private String code;
	private String name;
	private Boolean isManager;
	private Role role;
	
}
