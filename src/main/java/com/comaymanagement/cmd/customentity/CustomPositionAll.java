package com.comaymanagement.cmd.customentity;

import java.util.Set;

import com.comaymanagement.cmd.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class CustomPositionAll {
	private String id;
	private String name;
	private Boolean isManager;
	private Set<Role> roleList;

}
