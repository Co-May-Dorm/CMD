package com.comaymanagement.cmd.model;

import java.util.List;

import com.comaymanagement.cmd.entity.Option;
import com.comaymanagement.cmd.entity.Permission;
import com.comaymanagement.cmd.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class RoleDetailModel {
	private Integer id;
	private List<Option> options;
	private List<Permission> permissions;
	private Role role;
}
