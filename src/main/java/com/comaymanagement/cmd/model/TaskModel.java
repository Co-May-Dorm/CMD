package com.comaymanagement.cmd.model;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class TaskModel {
	
	private Integer id;
	private Integer creatorId;
	private String creatorName;
	private Integer recieverId;
	private String recieverName;
	private String statusName;
	private String title;
	private String departmentName;
	private String createDate;
	private String finishDate;
	private String description;

}
