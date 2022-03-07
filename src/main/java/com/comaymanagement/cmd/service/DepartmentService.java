package com.comaymanagement.cmd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.repository.IDepartmentRepository;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class DepartmentService {
	@Autowired
	DepartmentRepositoryImpl departmentRepository;

	public List<Department> findAllDepartmentByEmployeeId(String id) {
		return departmentRepository.findAllDepartmentByEmployeeId(id);
	}
	public ResponseEntity<Object> findAll(String name) {
		name = name == null ? "" : name.trim();
		List<CustomDepartmentAll> cusDepList = departmentRepository.findAll(name);
		
		if(cusDepList.size() > 0 ) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Successful",cusDepList));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error","Not found",""));
		}
		
		
	}
	
//	public ResponseEntity<Object> add(String json){
//		JsonMapper 
//		return null;
//	}
	
}
