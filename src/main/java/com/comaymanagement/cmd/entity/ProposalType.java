package com.comaymanagement.cmd.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "proposal_types")
public class ProposalType {
	@Id
	private String id;
	private String name;
	
	
	@OneToMany
	@JoinColumn(name="proposal_type_id")
	@JsonBackReference
	private Set<ProposalPermission> proposalPermissionList;
	
	@OneToMany
	@JoinColumn(name="proposal_type_id")
	@JsonBackReference
	private Set<Proposal> proposalList;
	
	@OneToMany
	@JoinColumn(name="proposal_type_id")
	@JsonBackReference
	private Set<ApprovalStep> approvalStepList;
	
}
