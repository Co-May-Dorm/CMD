package com.comaymanagement.cmd.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionModel {
	private Integer id;
	private String code;
	private String name;
	List<PermissionModel> permissions;
}
