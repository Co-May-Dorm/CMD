package com.comaymanagement.cmd.customentity;

import java.util.Set;

import com.comaymanagement.cmd.entity.Auth;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.RoleDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomRoleAll {
	private String id;
	private String name;
	private Set<Auth> authList;
	private Set<RoleDetail> roleDetailList ;
	private Position position;
}
