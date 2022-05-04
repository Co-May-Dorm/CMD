package com.comaymanagement.cmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.RoleDetailModel;
import com.comaymanagement.cmd.repositoryimpl.RoleDetailRepositoryImpl;
@Service
public class RoleDetailService {
	@Autowired
	RoleDetailRepositoryImpl roleDetailRepositoryImpl;
	
	public ResponseEntity<Object> findAllByRoleId(Integer roleId){
		RoleDetailModel cusRoleDetail = roleDetailRepositoryImpl.findAllByRoleId(roleId);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("","",cusRoleDetail));  
	}
}
