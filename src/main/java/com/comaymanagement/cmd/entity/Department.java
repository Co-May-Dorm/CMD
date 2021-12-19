package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	private String fatherDepartmentId;
	private String managerId;
	private String createBy;
	private String createDate;
	private String modifyBy;
	private String modifyDate;
	
	@OneToMany
	@JoinColumn(name="department_id")
	@JsonBackReference
	private Set<Position> positionList;
	
	@OneToMany
	@JoinColumn(name="department_id")
	@JsonBackReference
	private Set<ProposalPermission> proposalPermissionList;
	
	@OneToMany
	@JoinColumn(name="department_id")
	@JsonBackReference
	private Set<ApprovalStepDetail> approvalStepDetailList;
	

	@ManyToMany()
	@JoinTable(name = "departments_employees",
	joinColumns = {@JoinColumn(name = "department_id", referencedColumnName = "id")},
	inverseJoinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "id")})
	@JsonBackReference
	private Set<Employee> employeeList;
	
	
}
