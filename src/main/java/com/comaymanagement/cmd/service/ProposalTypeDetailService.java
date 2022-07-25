package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.ApprovalStep;
import com.comaymanagement.cmd.entity.ProposalTypeDetail;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.ApprovalStepModel;
import com.comaymanagement.cmd.model.ProposalTypeDetailModel;
import com.comaymanagement.cmd.repositoryimpl.ApprovalStepRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.ProposalTypeDetailRepositoryImpl;

@Service
public class ProposalTypeDetailService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Message message;
	
	@Autowired
	ProposalTypeDetailRepositoryImpl proposalTypeDetailReposiotory;
	@Autowired
	ApprovalStepRepositoryImpl approvalStepRepository;
	
	public ResponseEntity<Object> findById(Integer id){
		List<ProposalTypeDetail> proposalTypeDetails= new ArrayList<>();
		List<ApprovalStep> approvalSteps = new ArrayList<>();
		List<ProposalTypeDetailModel> proposalTypeDetailModels = new ArrayList<>();
		List<ApprovalStepModel> approvalStepModels = new ArrayList<>();
		
		approvalSteps = approvalStepRepository.findByProposalTypeId(id);
		
		proposalTypeDetails = proposalTypeDetailReposiotory.findById(id);
		
		if (proposalTypeDetails.size() > 0) {
			proposalTypeDetailModels = proposalTypeDetailReposiotory.toModel(proposalTypeDetails);
			approvalStepModels = approvalStepRepository.toModel(approvalSteps);
			Map<String, Object> result = new TreeMap<>();
			result.put("fields", proposalTypeDetailModels);
			result.put("steps", approvalStepModels);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "",result ));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Not found", ""));
		}
	}
	
}
