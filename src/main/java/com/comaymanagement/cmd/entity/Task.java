package com.comaymanagement.cmd.entity;

import javax.persistence.Column;
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
@Entity(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String code;
	private String title;
	private String description;
	@Column(name= "create_date")
	private String createDate;
	@Column(name= "finish_date")
	private String finishDate;
	
	@OneToOne()
	@JoinColumn(name = "creator_id", nullable = false)
	private Employee creator;

	@OneToOne()
	@JoinColumn(name = "receiver_id")
	private Employee receiver;

	@OneToOne()
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

}
