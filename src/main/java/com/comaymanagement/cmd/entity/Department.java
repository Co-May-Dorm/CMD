package com.comaymanagement.cmd.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department {

	@Id
	private String id;
	private String name;
	@Column(name="farther_department_id")
	private String fatherDepartmentId;
	@Column(name="manager_id")
	private String managerId;
	@Column(name="create_by")
	private String createBy;
	@Column(name="create_date")
	private String createDate;
	@Column(name="modify_by")
	private String modifyBy;
	@Column(name="modify_date")
	private String modifyDate;

	@OneToMany
	@JoinColumn(name = "department_id")
	@JsonBackReference
	private List<Position> positionList;

	@OneToMany
	@JoinColumn(name = "department_id")
	private List<ProposalPermission> proposalPermissionList;

	@OneToMany
	@JoinColumn(name = "department_id")
	private List<ApprovalStepDetail> approvalStepDetailList;

	@OneToMany
	@JoinColumn(name = "department_id")
	@JsonBackReference
	private List<Employee> employeeList;

}
