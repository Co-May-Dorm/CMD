package com.comaymanagement.cmd.model;

import java.util.List;

import javax.persistence.Entity;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Status;
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
@JsonInclude(Include.NON_NULL)
public class TaskModel {
	private Integer id;
	private EmployeeModel creator;
	private EmployeeModel receiver;
	private Status status;
	private String title;
	private List<DepartmentModel> department;
	private String createDate;
	private String finishDate;
	private String description;

}
