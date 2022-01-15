package com.comaymanagement.cmd.entity;

import javax.persistence.Entity;
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
	private String id;
	private String title;
	private String description;
	private String createDate;
	
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
