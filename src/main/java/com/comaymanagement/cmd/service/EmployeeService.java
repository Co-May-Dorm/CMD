package com.comaymanagement.cmd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.comaymanagement.cmd.constant.CMDConstrant;
import com.comaymanagement.cmd.constant.Message;
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
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.TeamRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class EmployeeService implements IGeneralService<Employee> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

	@Autowired
	EmployeeRepositoryImpl employeeRepository;

	@Autowired
	DepartmentRepositoryImpl departmentRepository;

	@Autowired
	PositionRepositoryImpl positionRepository;

	@Autowired
	TeamRepositoryImpl teamRepository;

	
	// Find all employee and search
	public ResponseEntity<Object> employeePaging(String page, String name, String dob, String email, String phone,
			String dep, String pos, String sort, String order) {
		Integer limit = new Integer(CMDConstrant.LIMIT);
		List<CustomEmployeeAll> cusEmpListTmp = new ArrayList<>();
		List<CustomEmployeeAll> cusEmpList = new ArrayList<>();
		name = name == null ? "" : name.trim();
		dob = dob == null ? "" : dob.trim();
		email = email == null ? "" : email.trim();
		phone = phone == null ? "" : phone.trim();
		dep = dep == null ? "" : dep.trim();
		pos = pos == null ? "" : pos.trim();
		page = page == null ? "1" : page.trim();
		// Caculator offset
		int offset = (Integer.parseInt(page) - 1) * limit;

		// Order by defaut
		if (sort == null || sort == "") {
			sort = "emp.id";
		}
		if (order == null || order == "") {
			order = "desc";
		}
		try {
			Integer totalItem = employeeRepository.countAllPaging(name, dob, email, phone, dep, pos, sort, order);
			Integer numberOfItemNeeded = 0;
			numberOfItemNeeded = totalItem < limit ? totalItem : limit; 
			while (cusEmpList.size() < numberOfItemNeeded) {
				offset = cusEmpList.size() == 0 ? offset : (offset + cusEmpList.size() + 1);
				limit = numberOfItemNeeded - cusEmpList.size();
				cusEmpListTmp = employeeRepository.findAll(name, dob, email, phone, dep, pos, sort, order, limit, offset);
				for(CustomEmployeeAll cusEmp : cusEmpListTmp) {
					cusEmpList.add(cusEmp);
				}
				cusEmpListTmp.clear();
			}
			Integer totalItemEmployee = employeeRepository.countAllPaging(name, dob, email, phone, dep, pos, sort, order);
			Pagination pagination = new Pagination();
			Map<String, Object> result = new TreeMap<>();
			pagination.setLimit(CMDConstrant.LIMIT);
			pagination.setPage(Integer.valueOf(page));
			pagination.setTotalItem(totalItemEmployee);
			
			result.put("pagination", pagination);
			result.put("employees", cusEmpList);
			if (cusEmpList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully:", result));
			} else {
				pagination.setPage(1);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", result));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in employeePaging() ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}
		
		
	}

	// Add and edit employee
	public ResponseEntity<Object> addEmployee(String json) {
		Employee emp = new Employee();
		User user = new User();
		String createDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
		String modifyDate = createDate;
		List<Position> positionList = new ArrayList<>();
		List<Team> teamList = new ArrayList<>();
		List<Department> departmentList = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectEmployee;
		JsonNode jsonObjectPosition;
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectTeam;
		JsonNode jsonLoginAccount;

		Integer id = -1;

		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectEmployee.get("positions");
			jsonObjectTeam = jsonObjectEmployee.get("teams");
			jsonObjectDepartment = jsonObjectEmployee.get("departments");
			jsonLoginAccount = jsonObjectEmployee.get("user");
//			Check employee code existed
			String code = jsonObjectEmployee.get("code").asText();
			String avatar = jsonObjectEmployee.get("avatar") != null ? jsonObjectEmployee.get("avatar").asText() : "";
			String gender = jsonObjectEmployee.get("gender") != null ? jsonObjectEmployee.get("gender").asText() : "";
			String dateOfBirth = jsonObjectEmployee.get("dateOfBirth") == null ? ""
					: jsonObjectEmployee.get("dateOfBirth").asText() == "null" ? ""
							: jsonObjectEmployee.get("dateOfBirth").asText();
			String email = jsonObjectEmployee.get("email") == null ? ""
					: jsonObjectEmployee.get("email").asText() == "null" ? ""
							: jsonObjectEmployee.get("email").asText();
			String phoneNumber = jsonObjectEmployee.get("phoneNumber") == null ? ""
					: jsonObjectEmployee.get("phoneNumber").asText() == "null" ? ""
							: jsonObjectEmployee.get("phoneNumber").asText();
			boolean isExisted = employeeRepository.checkEmployeeCodeExisted(id, code);

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", "Mã sinh viên này đã tồn tại!", ""));
			}
			emp.setCode(jsonObjectEmployee.get("code").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(avatar);
			emp.setGender(gender);
			emp.setDateOfBirth(dateOfBirth);
			emp.setEmail(email);
			emp.setPhoneNumber(phoneNumber);
			Boolean isEnableLogin = jsonLoginAccount.get("enableLogin").asBoolean();
			emp.setEnableLogin(isEnableLogin);
			if (isEnableLogin) {
				emp.setUsername(jsonLoginAccount.get("username").asText());
				emp.setPassword(CMDConstrant.PASSWORD);
			} else {
				emp.setUsername("");
				emp.setPassword("");
			}
			if (jsonObjectPosition.isArray()) {
				for (JsonNode p : jsonObjectPosition) {
					Position pos = new Position();
					pos.setId(Integer.valueOf(p.toString()));
					positionList.add(pos);
				}
			}
			if (jsonObjectTeam.isArray()) {
				for (JsonNode t : jsonObjectTeam) {
					Team team = new Team();
					team.setId(Integer.valueOf(t.toString()));
					teamList.add(team);
				}
			}
			
			if (jsonObjectDepartment.isArray()) {
				for (JsonNode d : jsonObjectDepartment) {
					Department department = new Department();
					department.setId(Integer.valueOf(d.toString()));
					departmentList.add(department);
				}
			}
			
			emp.setPositions(positionList);
			emp.setTeams(teamList);
			emp.setDepartments(departmentList);
			emp.setActiveFlag(true);
			emp.setActive(true);
			emp.setCreateDate(createDate);
			emp.setModifyDate(modifyDate);
			emp.setCreateBy(jsonObjectEmployee.get("createBy").asInt());
			emp.setModifyBy(jsonObjectEmployee.get("modifyBy").asInt());
			Integer idAdded = employeeRepository.add(emp);
			if (idAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idAdded + "", emp));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", idAdded + "", emp));

			}
		} catch (Exception e) {
			logger.error("Error has occured in addEmployee()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}

	}

	// API edit and clock account (isActive true || false)
	public ResponseEntity<Object> edit(String json) {
		Employee emp = new Employee();
		User user = new User();
		List<Position> positionList = new ArrayList<>();
		List<Team> teamList = new ArrayList<>();
		List<Department> departmentList = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectEmployee;
		JsonNode jsonObjectPosition;
		JsonNode jsonObjectTeam;
		JsonNode jsonObjectDepartment;
		JsonNode jsonLoginAccount;
		String modifyDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectEmployee.get("positions");
			jsonObjectTeam = jsonObjectEmployee.get("teams");
			jsonObjectDepartment = jsonObjectEmployee.get("departments");
			Integer id = jsonObjectEmployee.get("id") != null ? jsonObjectEmployee.get("id").asInt() : -1;
//			Check employee id existed
			String code = jsonObjectEmployee.get("code").asText();
			boolean isExisted = employeeRepository.checkEmployeeCodeExisted(id, code);
			if (isExisted) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", "Mã sinh viên này đã tồn tại!", ""));
			}
			jsonLoginAccount = jsonObjectEmployee.get("user");
			boolean active = jsonObjectEmployee.get("active").asBoolean();
			/* Check if active == false 
			Check if the employee is the head of the department, do not allow the lock */
			if(!active) {
				Employee empCheck = employeeRepository.findById(id);
				for(Position p : empCheck.getPositions()) {
					if(p.getIsManager()) {
						String message = Message.getMessage(2);
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new ResponseObject("Error", message, ""));
					}
				}
			}
			String avatar = jsonObjectEmployee.get("avatar") != null ? jsonObjectEmployee.get("avatar").asText() : "";
			String gender = jsonObjectEmployee.get("gender") != null ? jsonObjectEmployee.get("gender").asText() : "";
			String dateOfBirth = jsonObjectEmployee.get("dateOfBirth") == null ? ""
					: jsonObjectEmployee.get("dateOfBirth").asText() == "null" ? ""
							: jsonObjectEmployee.get("dateOfBirth").asText();
			String email = jsonObjectEmployee.get("email") == null ? ""
					: jsonObjectEmployee.get("email").asText() == "null" ? ""
							: jsonObjectEmployee.get("email").asText();
			String phoneNumber = jsonObjectEmployee.get("phoneNumber") == null ? ""
					: jsonObjectEmployee.get("phoneNumber").asText() == "null" ? ""
							: jsonObjectEmployee.get("phoneNumber").asText();
			emp.setId(jsonObjectEmployee.get("id").asInt());
			emp.setCode(jsonObjectEmployee.get("code").asText());
			emp.setName(jsonObjectEmployee.get("name").asText());
			emp.setAvatar(avatar);
			emp.setGender(gender);
			emp.setDateOfBirth(dateOfBirth);
			emp.setEmail(email);
			emp.setPhoneNumber(phoneNumber);
			emp.setActive(jsonObjectEmployee.get("active").asBoolean());
			Boolean isEnableLogin = jsonLoginAccount.get("enableLogin").asBoolean();
			emp.setEnableLogin(isEnableLogin);
			// Cannot edit password
			if (isEnableLogin) {
				emp.setUsername(jsonLoginAccount.get("username").asText());
				emp.setPassword(CMDConstrant.PASSWORD);
			} else {
				emp.setUsername("");
				emp.setPassword("");
			}
			for (JsonNode p : jsonObjectPosition) {
				Position pos = new Position();
				pos.setId(Integer.valueOf(p.toString()));
				positionList.add(pos);
			}
			if (jsonObjectTeam.isArray()) {
				for (JsonNode t : jsonObjectTeam) {
					Team team = new Team();
					team.setId(Integer.valueOf(t.toString()));
					teamList.add(team);
				}
			}
			if (jsonObjectDepartment.isArray()) {
				for (JsonNode t : jsonObjectDepartment) {
					Department department = new Department();
					department.setId(Integer.valueOf(t.toString()));
					departmentList.add(department);
				}
			}
			emp.setPositions(positionList);
			emp.setTeams(teamList);
			emp.setDepartments(departmentList);
			emp.setActiveFlag(true);
			emp.setActive(jsonObjectEmployee.get("active").asBoolean());
			emp.setCreateDate(jsonObjectEmployee.get("createDate").asText());
			emp.setModifyDate(modifyDate);
			emp.setCreateBy(jsonObjectEmployee.get("createBy").asInt());
			emp.setModifyBy(jsonObjectEmployee.get("modifyBy").asInt());
			Integer message = employeeRepository.edit(emp);
			if (message != 0) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message + "", emp));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", message + "", emp));

			}
		} catch (Exception e) {
			logger.error("Error has occured in edit()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}

	}

	// Delete employee by id
	public ResponseEntity<Object> delete(Integer id) {
		try {
			Employee emp = employeeRepository.findById(id);
			emp.setActive(false);
			emp.setActiveFlag(false);
			emp.getDepartments().clear();
			emp.getTeams().clear();
			emp.getPositions().clear();
			String updateStatus = employeeRepository.delete(emp);

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
	public ResponseEntity<Object> save(Employee t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Employee model) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseEntity<Object> save(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
