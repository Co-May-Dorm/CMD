package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="roles")
public class Role {
	@Id
	private String id;
	private String name;
	@OneToMany
	@JoinColumn(name="role_id")
	@JsonBackReference
	private Set<Auth> authList;
	
	@OneToMany
	@JoinColumn(name="role_id")
	@JsonBackReference
	private Set<RoleDetail> roleDetailList ;
	
	@OneToOne()
	@JoinColumn(name="position_id")
	private Position position;
}
