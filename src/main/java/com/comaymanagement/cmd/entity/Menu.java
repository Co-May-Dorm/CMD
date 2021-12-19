package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
	
	@Id
	private String id;
	
	private String parentId;
	private String url;
	private String name;
	private Integer orderIndex;
	private String createDate;
	private String updateDate;
	
	@OneToMany
	@JoinColumn(name="menu_id")
	@JsonBackReference
	private Set<Auth> authList;
	
}
