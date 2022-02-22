package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "approval_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStep {

	@Id
	private String id;
	@Column(name="approval_step_name")
	private String approvalStepName;
	@Column(name="approval_step_index")
	private Integer approvalStepIndex;

	@OneToOne()
	@JoinColumn(name = "proposal_type_id")
	private ProposalType proposalType;

	@OneToMany
	@JoinColumn(name = "approval_step_id")
	@JsonBackReference
	private Set<ApprovalStepDetail> approvalStepDetailList;
}
