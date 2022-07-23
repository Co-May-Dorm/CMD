package com.comaymanagement.cmd.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.comaymanagement.cmd.entity.DataType;
import com.comaymanagement.cmd.entity.ProposalType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "proposal_type_details")
@JsonInclude(Include.NON_NULL)
public class ProposalTypeDetailModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name="field_id")
	private String fieldId;
	@Column(name="field_name")
	private String fieldName;
	@OneToOne()
	@JoinColumn(name = "proposal_type_id")
	private ProposalType proposalType;
	@OneToOne()
	@JoinColumn(name = "data_type_id")
	private DataType dataType;
	
	List<ApprovalStepModel> approvalStepModels; 
}

