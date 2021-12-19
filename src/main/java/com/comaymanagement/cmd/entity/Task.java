package com.comaymanagement.cmd.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tasks")
public class Task {  
	@Id
	private String id;

	
	@OneToOne()
	@JoinColumn(name="creator_id",nullable = false)
	private Employee employee;
	
	@OneToMany(mappedBy = "task")
	@JsonManagedReference
	@Column(nullable = false)
	private Set<TaskDetail> taskDetailList = new HashSet<TaskDetail>();
	
	@OneToOne()
	@JoinColumn(name="status_id",nullable = false)
	private Status status;


}
