package com.comaymanagement.cmd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "task_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class TaskDetail {

	@Id
	private String id;
	private String title;
	private String description;
	private String createBy;
	private String modifyBy;
	private String createDate;
	private String modifyDate;

	
	@OneToOne()
	@JsonBackReference
	@JoinColumn(name = "task_id")
	private Task task;
	
	@OneToOne()
	@JoinColumn(name="receiver_id")
	private Employee employee;
	
	
}
