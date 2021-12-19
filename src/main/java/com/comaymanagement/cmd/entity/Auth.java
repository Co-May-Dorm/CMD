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

@Entity(name = "auth")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Auth {
	
	@Id
	private String id;
	private Boolean permission;
	private Boolean activeFlag;
	private String createDate;
	private String updateDate;
	
	@OneToOne()
	@JoinColumn(name="menu_id")
	private Menu menu;
	
	@OneToOne()
	@JoinColumn(name="role_id")
	private Role role;
}
