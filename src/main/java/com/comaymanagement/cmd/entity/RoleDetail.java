package com.comaymanagement.cmd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="role_details")
public class RoleDetail {
	@Id
	private String id;
	private String createBy;
	private String modify_by;
	private String createDate;
	private String modifyDate;
	
	@OneToOne()
	@JoinColumn(name="option_id")
	private Option option;
	
	@OneToOne()
	@JoinColumn(name="permission_id")
	private Permission permission;
	
	@OneToOne()
	@JoinColumn(name="role_id")
	private Role role;
}
