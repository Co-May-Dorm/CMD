package com.comaymanagement.cmd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.repository.IDepartmentRepository;

@Service
public class DepartmentService{
	@Autowired
	IDepartmentRepository departmentRepository;
	public List<Department> findAllDepartmentByEmployeeId( String id){
		return departmentRepository.findAllDepartmentByEmployeeId(id);
	}
	public List<Department> findAll(){
		return departmentRepository.findAll();
	}
	
}
