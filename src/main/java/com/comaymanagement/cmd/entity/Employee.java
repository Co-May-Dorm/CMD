package com.comaymanagement.cmd.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employees")
public class Employee {
	@Id
	private String id;
	private Integer unique_number;
	private String name;
	private String dateOfBirth;
	private String email;
	private String phoneNumber;
	private boolean activeFlag;
	private String managerId;
	private String createBy;
	private String modifyBy;
	private String createDate;
	private String modifyDate;
	private String avatar;
	private String gender;
	private String username;
	private String password;
	private boolean enableLogin;
	
	@OneToOne()
	@JoinColumn(name = "department_id")
	private Department department;

	@ManyToMany
	@JoinTable(name = "positions_employees", joinColumns = {
			@JoinColumn(name = "employee_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "position_id", referencedColumnName = "id") })
	private List<Position> positionList;

	@OneToMany
	@JoinColumn(name = "employee_id")
	private List<ProposalPermission> proposalPermissionList;

	@OneToMany
	@JoinColumn(name = "creator_id")
	private List<Task> taskListCreated;
	
	@OneToMany
	@JoinColumn(name = "receiver_id")
	private List<Task> taskListReceived;

	@OneToMany
	@JoinColumn(name = "creator_id")
	private List<Proposal> proposalList;

	@OneToMany
	@JoinColumn(name = "employee_id")
	private List<ApprovalStepDetail> approvalStepDetailList;

	@ManyToMany
	@JoinTable(name = "teams_employees", joinColumns = {
			@JoinColumn(name = "employee_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "team_id", referencedColumnName = "id") })
	private List<Team> teamList;

}
