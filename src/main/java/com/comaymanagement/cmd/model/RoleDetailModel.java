package com.comaymanagement.cmd.model;

import java.util.List;

import com.comaymanagement.cmd.entity.Option;
import com.comaymanagement.cmd.entity.Permission;
import com.comaymanagement.cmd.entity.Role;

public class RoleDetailModel {
	private Integer id;
	private List<Option> options;
	private List<Permission> permissions;
	private Role role;
}
