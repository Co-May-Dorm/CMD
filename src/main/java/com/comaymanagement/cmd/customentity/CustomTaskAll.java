package com.comaymanagement.cmd.customentity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTaskAll {
	
	@Id
	private String taskId;
	private Integer uniqueNumber;
	private String creatorId;
	private String creatorName;
	private String recieverId;
	private String recieverName;
	private String statusName;
	private String title;
	private String departmentName;
	private String createDate;
	private String finishDate;
}
