package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;
	@GetMapping("")
	public ResponseEntity<Object> findAll() {
		
		List<CustomDepartmentAll> customDepartments = new ArrayList<>();
		List<Department> departments = departmentService.findAll();
		for(Department d : departments) {
			CustomDepartmentAll custom = new CustomDepartmentAll();
			custom.setId(d.getId());
			custom.setName(d.getName());
			custom.setFatherDepartmentId(d.getFatherDepartmentId());
			custom.setPositionList(d.getPositionList());
			customDepartments.add(custom);
		}
		
		if (customDepartments.size() > 0 ) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("OK","Query produce successfully: ", customDepartments)
			);
		}else {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject("Not found","Error","")
			);
		}
	}
}
