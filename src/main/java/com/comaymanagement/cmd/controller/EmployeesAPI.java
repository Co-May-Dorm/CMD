package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;
import com.comaymanagement.cmd.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeesAPI {
//	 private final Logger logger = LoggerFactory.getLogger(this.getClass()); => Xong goi method ra
	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;
	// Create url find all employees
	@GetMapping(path = "", produces = "application/json")
	public ResponseEntity<Object> findAll() {
		
	List<CustomEmployeeAll> customEmployees = new ArrayList<>();
	List<Employee> employees = employeeService.findAll();
	
	for(Employee e : employees) {
		CustomEmployeeAll cEmp = new CustomEmployeeAll();
		cEmp.setId(e.getId());
		cEmp.setName(e.getName());
		cEmp.setDateOfBirth(e.getDateOfBirth());
		cEmp.setEmail(e.getEmail());
		cEmp.setPhoneNumber(e.getPhoneNumber());
		cEmp.setDepartmentList(e.getDepartmentList());
		cEmp.setPositionList(e.getPositionList());
		customEmployees.add(cEmp);
		
		
	}
		
		if (customEmployees.size() > 0 ) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("OK","Query produce successfully: ", customEmployees)
			);
		}else {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject("Not found","Error","")
			);
		}
	}
	// Create url find employees by id
	@GetMapping("/{id}")
	public Optional<Employee> FindByID(@PathVariable String id) {

		return employeeService.findById(id);
	}
	@GetMapping("/{id}/departments")
	public ResponseEntity<Object> findAllDepartmentByEmployeeId(@PathVariable String id) {
		
		List<Department> departments = departmentService.findAllDepartmentByEmployeeId(id);
		
		if (departments.size() > 0 ) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("OK","Query produce successfully: ", departments)
			);
		}else {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject("Not found","Department not found with Employeeid= " + id,"")	
			);
		}
	}
	@GetMapping("/flag/{f}")
	public List<Employee> FindByActiveFlag(@PathVariable Boolean f) {
		return employeeService.findByActiveFlag(f);
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
