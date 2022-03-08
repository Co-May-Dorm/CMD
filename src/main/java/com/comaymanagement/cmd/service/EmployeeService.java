package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class EmployeeService implements IGeneralService<Employee> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmployeeRepositoryImpl employeeRepository;

	// Find all employee and search
	public ResponseEntity<Object> employeePaging(String page, String name, String dob, String email, String phone, String dep,
			String pos, String sort, String order) {
		name = name == null ? "" : name.trim();
		dob = dob == null ? "" : dob.trim();
		email = email == null ? "" : email.trim();
		phone = phone == null ? "" : phone.trim();
		dep = dep == null ? "" : dep.trim();
		pos = pos == null ? "" : pos.trim();
		page = page == null ? "1" : page.trim();
		int limit = 15;
			// Caculator offset
			int offset = (Integer.parseInt(page) - 1) * limit;

			// Order by defaut
			if (sort == null || sort == "") {
				sort = "uniqueNumber";
			}
			if (order == null || order == "") {
				order = "desc";
			}
		List<CustomEmployeeAll> cusEmpList = employeeRepository.employeePaging(name, dob, email, phone, dep, pos, sort, order,
				limit, offset);
		Pagination pagination = new Pagination();
		Map<String, Object> result = new TreeMap<>();
		result.put("pagination", pagination);
		result.put("employees", cusEmpList);
		if (cusEmpList.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully:", result));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found", "Not found", ""));
		}
	}

	// Add and edit employee
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

			JsonNode tmp = jsonObjectEmployee.get("uniqueNumber");
//			Check employee id existed
			boolean isExisted = employeeRepository.checkEmployeeIdExisted(jsonObjectEmployee.get("id").asText());

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", "Mã sinh viên này đã tồn tại!", ""));
			}
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
			emp.setEnableLogin(jsonLoginAccount.get("enableLogin").asBoolean());
			// (If uniqueNumber == "") => add, else => edit (Password editing is not
			// allowed)
			if (emp.isEnableLogin()) {
				emp.setUsername(jsonLoginAccount.get("username").asText());
				emp.setPassword(DefaultPassword.PASSWORD);
			}
			for (JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(p.get("id").asText());
				positionList.add(pos);
			}
			emp.setPositionList(positionList);

		} catch (Exception e) {
			logger.error("paggingAllEmployee()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}
		String message = employeeRepository.add(emp);
		if (message != "") {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message + "", "employee" + emp));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", message + "", emp));

		}

	}
	public ResponseEntity<Object> edit(String json) {
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
//			Check employee id existed
			jsonObjectPosition = jsonObjectEmployee.get("positionList");
			jsonObjectDepartment = jsonObjectEmployee.get("department");
			dep.setId(jsonObjectDepartment.get("id").asText());
			jsonLoginAccount = jsonObjectEmployee.get("user");
			emp.setUniqueNumber(jsonObjectEmployee.get("uniqueNumber").asInt());
			emp.setId(jsonObjectEmployee.get("id").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(jsonObjectEmployee.get("avatar").asText());
			emp.setGender(jsonObjectEmployee.get("gender").asText());
			emp.setDateOfBirth(jsonObjectEmployee.get("dateOfBirth").asText());
			emp.setEmail(jsonObjectEmployee.get("email").asText());
			emp.setPhoneNumber(jsonObjectEmployee.get("phoneNumber").asText());
			emp.setDepartment(dep);
			emp.setEnableLogin(jsonLoginAccount.get("enableLogin").asBoolean());
			// (If uniqueNumber == "") => add, else => edit (Password editing is not
			// allowed)
			emp.setUsername(jsonLoginAccount.get("username").asText());
			for (JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(p.get("id").asText());
				positionList.add(pos);
			}
			emp.setPositionList(positionList);

		} catch (Exception e) {
			logger.error("paggingAllEmployee()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}
		String message = employeeRepository.edit(emp);
		if (message != "") {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message + "", emp));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", message + "", emp));

		}

	}
	// Delete employee by id
	public ResponseEntity<Object> delete(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		try {
			JsonNode jsonObjectEmployee = jsonMapper.readTree(json);
			String id = jsonObjectEmployee.get("id").asText();
			String updateStatus = employeeRepository.delete(id);

			if (updateStatus.equals("1")) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", updateStatus + "", ""));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", updateStatus + "", ""));

			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "", ""));

		}

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
