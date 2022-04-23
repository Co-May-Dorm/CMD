package com.comaymanagement.cmd.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
public class Employee implements Comparable<Employee>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String code;
	private String name;
	@Column(name="date_of_birth")
	private String dateOfBirth;
	private String email;
	@Column(name="phone_number")
	private String phoneNumber;
	@Column(name="active_flag")
	private boolean activeFlag;
	@Column(name="create_by")
	private Integer createBy;
	@Column(name="modify_by")
	private Integer modifyBy;
	@Column(name="create_date")
	private String createDate;
	@Column(name="modify_date")
	private String modifyDate;
	private String avatar;
	private String gender;
	private String username;
	private String password;
	@Column(name="enable_login")
	private boolean enableLogin;
	@Column(name="is_active")
	private boolean isActive;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany()
	@JoinTable(name = "departments_employees", joinColumns = {
			@JoinColumn(name = "employee_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "department_id", referencedColumnName = "id") })
	private List<Department> departments;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany()
	@JoinTable(name = "positions_employees", joinColumns = {
			@JoinColumn(name = "employee_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "position_id", referencedColumnName = "id") })
	private List<Position> positions;

	@OneToMany
	@JoinColumn(name = "employee_id")
	private List<ProposalPermission> proposalPermissions;

	@OneToMany
	@JoinColumn(name = "creator_id")
	private List<Task> taskListCreated;
	
	@OneToMany
	@JoinColumn(name = "receiver_id")
	private List<Task> taskListReceived;

	@OneToMany
	@JoinColumn(name = "creator_id")
	private List<Proposal> proposals;

	@OneToMany
	@JoinColumn(name = "employee_id")
	private List<ApprovalStepDetail> approvalStepDetails;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	@JoinTable(name = "teams_employees", joinColumns = {
			@JoinColumn(name = "employee_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "team_id", referencedColumnName = "id") })
	private List<Team> teams;

	@Override
	public int compareTo(Employee o) {
		// we sort objects on the basis of Student Id
		return (o.id - this.id);
	}

}
