package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.constant.DefaultPassword;
import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.customentity.User;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class EmployeeService implements IGeneralService<Employee> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmployeeRepositoryImpl employeeRepository;

	// Find all employee and search
	public ResponseEntity<Object> employeePaging(String name, String dob, String email, String phone, String dep,
			String pos, String sort, String order, Integer limit, Integer offset) {
		Set<Employee> employeeList = employeeRepository.employeePaging(name, dob, email, phone, dep, pos, sort, order,
				limit, offset);
		Set<CustomEmployeeAll> cusEmpList = new HashSet<>();
		for (Employee e : employeeList) {
			CustomEmployeeAll cusEmp = new CustomEmployeeAll();
			CustomDepartmentAll cusDep = new CustomDepartmentAll();
			List<CustomPositionAll> cusPositionList = new ArrayList<>();

			cusDep.setId(e.getDepartment().getId());
			cusDep.setName(e.getDepartment().getName());
			cusDep.setManagerId(e.getDepartment().getManagerId());

			// Add position list
			for (Position p : e.getPositionList()) {
				CustomPositionAll cusPos = new CustomPositionAll();
				cusPos.setId(p.getId());
				cusPos.setName(p.getName());
				cusPos.setIsManager(p.getIsManager());
				cusPos.setRoleId(p.getRoleId());
				cusPositionList.add(cusPos);
			}
			User user = new User();
			user.setUsername(e.getUsername());
			user.setEnableLogin(e.isEnableLogin());

			cusEmp.setUniqueNumber(e.getUniqueNumber());
			cusEmp.setId(e.getId());
			cusEmp.setName(e.getName());
			cusEmp.setAvatar(e.getAvatar());
			cusEmp.setGender(e.getGender());
			cusEmp.setDateOfBirth(e.getDateOfBirth());
			cusEmp.setEmail(e.getEmail());
			cusEmp.setPhoneNumber(e.getPhoneNumber());
			cusEmp.setDepartment(cusDep);
			cusEmp.setPositionList(cusPositionList);
			cusEmp.setUser(user);

			cusEmpList.add(cusEmp);
		}
		if (cusEmpList.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully:", cusEmpList));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found", "Not found", ""));
		}
	}
	public ResponseEntity<Object> addEmployee(String json) {
		Employee emp = new Employee();
		User user = new User();
		List<Position> positionList = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectEmployee;
		JsonNode jsonObjectPosition;
		JsonNode jsonObjectDepartment;
		JsonNode jsonLoginAccount;
		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectEmployee.get("positionList");
			jsonObjectDepartment = jsonObjectEmployee.get("department");
			dep.setId(jsonObjectDepartment.get("id").asText());
			jsonLoginAccount = jsonObjectEmployee.get("user");
			emp.setId(jsonObjectEmployee.get("id").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(jsonObjectEmployee.get("avatar").asText());
			emp.setGender(jsonObjectEmployee.get("gender").asText());
			emp.setDateOfBirth(jsonObjectEmployee.get("dateOfBirth").asText());
			emp.setEmail(jsonObjectEmployee.get("email").asText());
			emp.setPhoneNumber(jsonObjectEmployee.get("phoneNumber").asText());
			emp.setDepartment(dep);
			emp.setUsername(jsonLoginAccount.get("username").asText());
			emp.setEnableLogin(jsonLoginAccount.get("enableLogin").asBoolean());
			if(emp.isEnableLogin()) {
				emp.setPassword(DefaultPassword.PASSWORD);
			}
			for(JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(p.get("id").asText());
				positionList.add(pos);
			}
			emp.setPositionList(positionList);
			
		} catch (Exception e) {
			logger.error("paggingAllEmployee()",e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", e.getMessage(), ""));
		}
		String id = employeeRepository.addEmployee(emp);
		if(id!="") {
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("OK", id+"", emp));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error",id+"",emp));

		}
	}
	
	public String updateEmployee(Employee emp) {
		return employeeRepository.updateEmployee(emp);
	}
	@Override
	public Iterable<Employee> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Employee> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee save(Employee t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Employee model) {
		// TODO Auto-generated method stub

	}

}
