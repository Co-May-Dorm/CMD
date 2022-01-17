package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.apache.catalina.connector.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.DepartmentService;
import com.comaymanagement.cmd.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class EmployeesAPI {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmployeeService employeeService;
	@Autowired
	DepartmentService departmentService;

	// Create url find all employees
//	@GetMapping(path = "", produces = "application/json")
//	public ResponseEntity<Object> findAll() {
//
//		List<CustomEmployeeAll> customEmployees = new ArrayList<>();
//		List<Employee> employees = employeeService.findAll();
//
//		for (Employee e : employees) {
//			CustomEmployeeAll cEmp = new CustomEmployeeAll();
//			cEmp.setId(e.getId());
//			cEmp.setName(e.getName());
//			cEmp.setDateOfBirth(e.getDateOfBirth());
//			cEmp.setEmail(e.getEmail());
//			cEmp.setPhoneNumber(e.getPhoneNumber());
//			cEmp.setDepartmentId(e.getDepartmentId());
//			cEmp.setPositionList(e.getPositionList());
//			customEmployees.add(cEmp);
//
//		}
//
//		if (customEmployees.size() > 0) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject("OK", "Query produce successfully: ", customEmployees));
//		} else {
//
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found", "Error", ""));
//		}
//	}

	// Create url find employees by id
//	@GetMapping("/{id}")
//	public Optional<Employee> FindByID(@PathVariable String id) {
//
//		return employeeService.findById(id);
//	}
//
//	@GetMapping("/{id}/departments")
//	public ResponseEntity<Object> findAllDepartmentByEmployeeId(@PathVariable String id) {
//
//		List<Department> departments = departmentService.findAllDepartmentByEmployeeId(id);
//
//		if (departments.size() > 0) {
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject("OK", "Query produce successfully: ", departments));
//		} else {
//
//			return ResponseEntity.status(HttpStatus.NOT_FOUND)
//					.body(new ResponseObject("Not found", "Department not found with Employeeid= " + id, ""));
//		}
//	}
//
//	@GetMapping("/flag/{f}")
//	public List<Employee> FindByActiveFlag(@PathVariable Boolean f) {
//		return employeeService.findByActiveFlag(f);
//	}

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
				@RequestParam(value="limit", required = false) Integer limit,
				@RequestParam(value="offset", required = false) Integer offset
				){
		name="";
		dob="";
		email="";
		phone="";
		dep="";
		pos="";
		
//		if(name == null) {
//			System.out.println("null");
//		}
//		if(name.equals("null")) {
//			System.out.println();
//		}
		//		page = page.trim();
//		name = name.trim();
//		dob = dob.trim();
//		email = email.trim();
//		phone = phone.trim();
//		dep = dep.trim();
//		pos = pos.trim();
//		sort = sort.trim();
//		order = order.trim();
		// Fix number of employee per page
		limit = 15;
		//Caculator offset
		offset = (Integer.parseInt(page) - 1) * limit;

		if(sort==null || sort == "") {
			sort = "emp.unique_number";
		}
		if(order == null || order == "") {
			order = "desc";
		}
		List<Employee> employees= employeeService.employeePaging(name, dob, email, phone, dep, pos, sort, order, limit, offset);
		if(employees.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:", employees));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Department not found with Employeeid= " + name, ""));
		}
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
