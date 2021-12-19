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

@Entity(name = "data_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataType {
	@Id
	private String id;
	private String name;
	private String description;
	
	@OneToMany
	@JoinColumn(name="data_type_id")
	@JsonBackReference
	private Set<ProposalTypeDetail> proposalTypeDetailList;
}
