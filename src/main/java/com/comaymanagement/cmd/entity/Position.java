package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

@Entity(name = "positions")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(name="is_manager")
	private Boolean isManager;
	@Column(name="create_by")
	private String createBy;
	@Column(name="create_date")
	private String createDate;
	@Column(name="modify_by")
	private String modifyBy;
	@Column(name="modify_date")
	private String modifyDate;
	
	@OneToOne
	@JoinColumn(name="role_id")
	@JsonBackReference
	private Role role;

	@OneToOne()
	@JoinColumn(name = "department_id")
	@JsonBackReference
	private Department department;
	
	@OneToOne()
	@JoinColumn(name = "team_id")
	@JsonBackReference
	Team team;
	
	@ManyToMany()
	@JoinTable(name = "positions_employees", joinColumns = {
			@JoinColumn(name = "position_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "employee_id", referencedColumnName = "id") })
	@JsonBackReference
	private Set<Employee> employeeList;

	@OneToMany
	@JoinColumn(name = "position_id")
	@JsonBackReference
	private Set<ApprovalStepDetail> approvalStepDetailList;

	@OneToMany
	@JoinColumn(name = "position_id")
	@JsonBackReference
	private Set<ProposalPermission> proposalPermissionList;

}
