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
				sort = "id";
			}
			if (order == null || order == "") {
				order = "desc";
			}
		Pagination pagination = new Pagination();
		Map<String, Object> result = new TreeMap<>();
		Integer total = employeeRepository.getTotal(name, dob, email, phone, dep, pos);
		pagination.setLimit(limit);
		pagination.setPage(page);
		pagination.setTotalItem(total);
		List<CustomEmployeeAll> cusEmpList = employeeRepository.employeePaging(name, dob, email, phone, dep, pos, sort, order,
				limit, offset);
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
		Integer id = -1;
		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectEmployee.get("positionList");
			jsonObjectDepartment = jsonObjectEmployee.get("department");
			jsonLoginAccount = jsonObjectEmployee.get("user");
//			Check employee id existed
			String code = jsonObjectEmployee.get("code").asText();
			boolean isExisted = employeeRepository.checkEmployeeIdExisted(id, code);
			
			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", "Mã sinh viên này đã tồn tại!", ""));
			}
			emp.setCode(jsonObjectEmployee.get("code").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(jsonObjectEmployee.get("avatar").asText());
			emp.setGender(jsonObjectEmployee.get("gender").asText());
			emp.setDateOfBirth(jsonObjectEmployee.get("dateOfBirth").asText());
			emp.setEmail(jsonObjectEmployee.get("email").asText());
			emp.setPhoneNumber(jsonObjectEmployee.get("phoneNumber").asText());
			Boolean isEnableLogin = jsonLoginAccount.get("enableLogin").asBoolean();
			if (isEnableLogin) {
				emp.setUsername(jsonLoginAccount.get("username").asText());
				emp.setPassword(DefaultPassword.PASSWORD);
			}
			for (JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(p.get("id").asInt());
				positionList.add(pos);
			}
			dep.setId(jsonObjectDepartment.get("id").asInt());
			emp.setPositionList(positionList);
			emp.setDepartment(dep);
		} catch (Exception e) {
			logger.error("Error has occured in addEmployee()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}
		Integer idAdded = employeeRepository.add(emp);
		if (idAdded != -1) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idAdded + "", "employee" + emp));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", idAdded + "", emp));

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
			jsonObjectPosition = jsonObjectEmployee.get("positionList");
			jsonObjectDepartment = jsonObjectEmployee.get("department");
			Integer id = jsonObjectEmployee.get("id") != null ? jsonObjectEmployee.get("id").asInt() : null;
			if(id == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "id cannot be null", ""));
			}
//			Check employee id existed
			boolean isExisted = employeeRepository.checkEmployeeIdExisted(id, jsonObjectEmployee.get("code").asText());
			if (isExisted) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", "Mã sinh viên này đã tồn tại!", ""));
			}
			jsonLoginAccount = jsonObjectEmployee.get("user");
			emp.setId(jsonObjectEmployee.get("id").asInt());
			emp.setCode(jsonObjectEmployee.get("code").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(jsonObjectEmployee.get("avatar").asText());
			emp.setGender(jsonObjectEmployee.get("gender").asText());
			emp.setDateOfBirth(jsonObjectEmployee.get("dateOfBirth").asText());
			emp.setEmail(jsonObjectEmployee.get("email").asText());
			emp.setPhoneNumber(jsonObjectEmployee.get("phoneNumber").asText());
			emp.setEnableLogin(jsonLoginAccount.get("enableLogin").asBoolean());
			// (If uniqueNumber == "") => add, else => edit (Password editing is not
			// allowed)
			emp.setUsername(jsonLoginAccount.get("username").asText());
			for (JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(p.get("id").asInt());
				positionList.add(pos);
			}
			dep.setId(jsonObjectDepartment.get("id").asInt());
			emp.setPositionList(positionList);
			emp.setDepartment(dep);

		} catch (Exception e) {
			logger.error("Error has occured in edit()", e);
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
			Integer id = jsonObjectEmployee.get("id").asInt();
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
