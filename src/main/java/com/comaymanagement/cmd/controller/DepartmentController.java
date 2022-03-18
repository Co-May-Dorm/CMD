package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;

	@GetMapping("")
	public ResponseEntity<Object> findAll(@RequestParam(value = "name", required = false) String name) {
		return departmentService.findAll(name);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody String json){
		return departmentService.save(json);
	}
//	
//	public ResponseEntity<Object> edit(@RequestBody String json){
//		return departmentService.add(json);
//	}
}
