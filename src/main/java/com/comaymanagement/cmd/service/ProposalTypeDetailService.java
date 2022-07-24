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
import com.comaymanagement.cmd.entity.ProposalTypeDetail;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.ProposalTypeDetailModel;
import com.comaymanagement.cmd.repositoryimpl.ProposalTypeDetailRepositoryImpl;

@Service
public class ProposalTypeDetailService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Message message;
	
	@Autowired
	ProposalTypeDetailRepositoryImpl proposalTypeDetailReposiotory;
//	public ResponseEntity<Object> findById(Integer id){
//		List<ProposalTypeDetail> proposalTypeDetails= new ArrayList<>();
//		proposalTypeDetails = proposalTypeDetailReposiotory.findById(id);
//		List<ProposalTypeDetailModel> proposalTypeDetailModels = new ArrayList<>();
//		if (proposalTypeDetails.size() > 0) {
//			List<ProposalTypeDetailModel> proposalTypeDetailModel = proposalTypeDetailReposiotory.toModel(proposalTypeDetails);
//			Map<String, Object> result = new TreeMap<>();
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "", ));
//		} else {
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Not found", proposalTypeDetails));
//		}
//	}
}
