package com.comaymanagement.cmd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;
import com.comaymanagement.cmd.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class EmployeesController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;

	@GetMapping(value = "", produces = "application/json")
	public ResponseEntity<Object> paggingAllEmployee(
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "dob", required = false) String dob,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "dep", required = false) String dep,
			@RequestParam(value = "pos", required = false) String pos,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "order", required = false) String order) {
		return employeeService.employeePaging(page,name, dob, email, phone, dep, pos, sort, order);
	}

	@PostMapping(value = "/add")
	@ResponseBody
	public ResponseEntity<Object> addEmployee(@RequestBody String json) {
		return employeeService.addEmployee(json);
	}

	@PostMapping(value = "/edit")
	@ResponseBody
	public ResponseEntity<Object> editEmployee(@RequestBody String json) {
		return employeeService.edit(json);
	}

	@PutMapping(value = "/delete")
	@ResponseBody
	public ResponseEntity<Object> deleteEmployee(@RequestBody String json) {
		return employeeService.delete(json);
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
