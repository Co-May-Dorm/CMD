package com.comaymanagement.cmd.entity;

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

@Entity(name = "approval_step_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStepDetail {
	
	@Id
	private String id;
	
	@OneToOne()
	@JoinColumn(name="approval_step_id",insertable=false, updatable=false)
	private ApprovalStep approvalStep;
	
	@OneToOne()
	@JoinColumn(name="department_id")
	private Department department;
	
	@OneToOne()
	@JoinColumn(name="postion_id")
	private Position position;
	
	@OneToOne()
	@JoinColumn(name="employee_id")
	private Employee employee;
}
