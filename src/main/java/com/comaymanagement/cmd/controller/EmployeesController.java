package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.customentity.User;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;
import com.comaymanagement.cmd.service.EmployeeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class EmployeesController {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;
	
	
	@GetMapping(value= "",produces = "application/json")
	public  ResponseEntity<Object> paggingAllEmployee(
				@RequestParam(value="page",required = false) String page, 
				@RequestParam(value="name",required = false) String name, 
				@RequestParam(value="dob",required = false) String dob, 
				@RequestParam(value="email", required = false) String email,  
				@RequestParam(value="phone", required = false) String phone, 
				@RequestParam(value="dep", required = false) String dep, 
				@RequestParam(value="pos", required = false) String pos,
				@RequestParam(value="sort", required = false) String sort,
				@RequestParam(value="order", required = false) String order,
				@RequestParam(value="limit", required = false) Integer limit
				){
		name = name == null ? "" : name.trim();
		dob = dob == null ? "" : dob.trim();
		email = email == null ? "" : email.trim();
		phone = phone == null ? "" : phone.trim();
		dep = dep == null ? "" : dep.trim();
		pos = pos == null ? "" : pos.trim();
		page = page == null ? "1" : page.trim();
		ResponseEntity<Object> cusEmployeeList;
		// Fix number of employee per page
		limit = 15;
		try {
			//Caculator offset
			int offset = (Integer.parseInt(page) - 1) * limit;
			
			// Order by defaut
			if(sort==null || sort == "") {
				sort = "uniqueNumber";
			}
			if(order == null || order == "") {
				order = "desc";
			}
			//Call to service
			cusEmployeeList= employeeService.employeePaging(name, dob, email, phone, dep, pos, sort, order, limit, offset);
			
		} catch (Exception e) {
			logger.error("paggingAllEmployee()",e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", e.getMessage(), ""));
		}
		return cusEmployeeList;
	}
	 
	
	@PostMapping(value= "",produces = "application/json")
	public ResponseEntity<Object> addEmployee(@RequestParam String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectEmployee;
		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
		
	}
	
	// Example return ResponseEntity

//	@GetMapping("/{id}")
//	public ResponseEntity<Object> findById(@PathVariable Long id) {
//		Optional<Produce> produce = produceRepository.findById(id);
//		
//		if (produce.isPresent()) {
//			return ResponseEntity.status(HttpStatus.OK).body(
//					new ResponseObject("OK","Query produce successfully: ", produce)
//			);
//		}else {
//			
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//				new ResponseObject("Not found","Produce not found with id: " + id,"")	
//			);
//		}
//		
//	}

//	@PutMapping("/update/{id}")
//	public ResponseEntity<ResponseObject> updateProduce(@RequestBody Produce produceUpdate,@PathVariable Long id) {
//		Optional<Produce>  updateProduce = produceRepository.findById(id);
//		if(updateProduce.isPresent()) {
//			updateProduce.map(produce -> {
//			
//					produce.setProduceName(produceUpdate.getProduceName());
//					produce.setYear(produceUpdate.getYear());
//					produce.setPrice(produceUpdate.getPrice());
//					return produceRepository.save(produce);
//
//				});
//
//			return ResponseEntity.status(HttpStatus.OK).body(
//					new ResponseObject("OK","Edited!", "")
//					);
//		}else {
//			return ResponseEntity.status(HttpStatus.OK).body(
//					new ResponseObject("Not Exists","Produce not exists", "")
//					);
//		}
//		
//					
//		
//	}
}
