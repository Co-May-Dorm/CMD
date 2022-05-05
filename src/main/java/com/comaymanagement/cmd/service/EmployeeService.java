package com.comaymanagement.cmd.service;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.comaymanagement.cmd.constant.CMDConstrant;
import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.model.EmployeeModel;
import com.comaymanagement.cmd.model.User;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.TeamRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmployeeRepositoryImpl employeeRepository;

	@Autowired
	DepartmentRepositoryImpl departmentRepository;

	@Autowired
	PositionRepositoryImpl positionRepository;

	@Autowired
	TeamRepositoryImpl teamRepository;

	@Autowired
	Message message;



	// Find all employee and search
	public ResponseEntity<Object> employeePaging(String page, String name, String dob, String email, String phone,
			String dep, String pos, String sort, String order) {
		Integer limit = new Integer(CMDConstrant.LIMIT);
		Set<EmployeeModel> employeeModelSetTMP = new LinkedHashSet<>();
		Set<EmployeeModel> employeeModelSet = new LinkedHashSet<>();
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
			Integer numberDuplicate = numberOfItemNeeded;
			while (employeeModelSet.size() < numberOfItemNeeded) {
				offset = employeeModelSet.size() == 0 ? offset : (offset + employeeModelSet.size() + numberDuplicate);
				limit = numberOfItemNeeded - employeeModelSet.size();
				employeeModelSetTMP = employeeRepository.findAll(name, dob, email, phone, dep, pos, sort, order, limit,
						offset);
				for (EmployeeModel employeeModel : employeeModelSetTMP) {
					employeeModelSet.add(employeeModel);
				}
				employeeModelSetTMP.clear();
			}
			Integer totalItemEmployee = employeeRepository.countAllPaging(name, dob, email, phone, dep, pos, sort,
					order);
			Pagination pagination = new Pagination();
			Map<String, Object> result = new TreeMap<>();
			pagination.setLimit(CMDConstrant.LIMIT);
			pagination.setPage(Integer.valueOf(page));
			pagination.setTotalItem(totalItemEmployee);

			result.put("pagination", pagination);
			result.put("employees", employeeModelSet);
			if (employeeModelSet.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successfully:", result));
			} else {
				pagination.setPage(1);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("ERROR", "Not found", result));
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
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectTeam;
		JsonNode jsonLoginAccount;

		Integer id = -1;

		try {
			jsonObjectEmployee = jsonMapper.readTree(json);
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
//			if (jsonObjectPosition.isArray()) {
//				for (JsonNode p : jsonObjectPosition) {
//					Position pos = new Position();
//					pos.setId(Integer.valueOf(p.toString()));
//					positionList.add(pos);
//				}
//			}
//			if (jsonObjectTeam.isArray()) {
//				for (JsonNode t : jsonObjectTeam) {
//					Team team = new Team();
//					team.setId(Integer.valueOf(t.toString()));
//					teamList.add(team);
//				}
//			}
//			
//			if (jsonObjectDepartment.isArray()) {
//				for (JsonNode d : jsonObjectDepartment) {
//					Department department = new Department();
//					department.setId(Integer.valueOf(d.toString()));
//					departmentList.add(department);
//				}
//			}
//			
//			for (JsonNode p : jsonObjectPosition) {
//				Position pos = new Position();
//				pos.setId(p.get("id").asInt());
//				positionList.add(pos);
//			}
			for (JsonNode t : jsonObjectTeam) {
				Team team = new Team();
				team.setId(t.get("id").asInt());
				
				Position pos = new Position();
				pos.setId(t.get("position").get("id").asInt());
				positionList.add(pos);
				teamList.add(team);
			}

			for (JsonNode d : jsonObjectDepartment) {
				Department department = new Department();
				department.setId(d.get("id").asInt());
				Position pos = new Position();
				pos.setId(d.get("position").get("id").asInt());
				positionList.add(pos);
				departmentList.add(department);
			}

			emp.setPositions(positionList);
			emp.setTeams(teamList);
			emp.setDepartments(departmentList);
			emp.setActiveFlag(true);
			emp.setActive(true);
			emp.setCreateDate(createDate);
			emp.setModifyDate(modifyDate);
			emp.setCreateBy(jsonObjectEmployee.get("createBy").asInt());
			emp.setModifyBy(jsonObjectEmployee.get("createBy").asInt());
			Integer idAdded = employeeRepository.add(emp);
			if (idAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idAdded + "", emp));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", idAdded + "", emp));

			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in addEmployee()", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("Error", e.getMessage(), ""));
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
			/*
			 * Check if active == false Check if the employee is the head of the department,
			 * do not allow the lock
			 */
			if (!active) {
				Employee empCheck = employeeRepository.findById(id);
				for (Position p : empCheck.getPositions()) {
					if (p.getIsManager()) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(new ResponseObject("Error", message.getMessageByItemCode("EMPE1"), ""));
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
//			if (jsonObjectPosition.isArray()) {
//				for (JsonNode p : jsonObjectPosition) {
//					Position pos = new Position();
//					pos.setId(Integer.valueOf(p.toString()));
//					positionList.add(pos);
//				}
//			}
//			if (jsonObjectTeam.isArray()) {
//				for (JsonNode t : jsonObjectTeam) {
//					Team team = new Team();
//					team.setId(Integer.valueOf(t.toString()));
//					teamList.add(team);
//				}
//			}
//			if (jsonObjectDepartment.isArray()) {
//				for (JsonNode t : jsonObjectDepartment) {
//					Department department = new Department();
//					department.setId(Integer.valueOf(t.toString()));
//					departmentList.add(department);
//				}
//			}
//			for (JsonNode p : jsonObjectPosition) {
//				Position pos = new Position();
//				pos.setId(p.get("id").asInt());
//				positionList.add(pos);
//			}
			for (JsonNode t : jsonObjectTeam) {
				Team team = new Team();
				team.setId(t.get("id").asInt());
				Position pos = new Position();
				pos.setId(t.get("position").get("id").asInt());
				positionList.add(pos);
				teamList.add(team);
			}
			for (JsonNode d : jsonObjectDepartment) {
				Department department = new Department();
				department.setId(d.get("id").asInt());
				Position pos = new Position();
				pos.setId(d.get("position").get("id").asInt());
				positionList.add(pos);
				departmentList.add(department);
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
			LOGGER.error("Error has occured in edit()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}

	}

	// Delete employee by id
	public ResponseEntity<Object> delete(Integer id) {
		try {
			Employee emp = employeeRepository.findById(id);
			for (Position p : emp.getPositions()) {
				if (p.getIsManager()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject("ERROR", message.getMessageByItemCode("EMPE2"), ""));
				}
			}
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
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "",e));

		}

	}

	public ResponseEntity<Object> importEmployees(MultipartFile multipartFile, Integer creatorId) {

		try {
			Path paths = CMDConstrant.path;
			String path = System.getProperty("user.dir");
			int length = path.length() - path.indexOf("CMD");
			if(length > 3) {
				path = CMDConstrant.path.toAbsolutePath().toString();
			}
			File file = new File(path + "/src/main/resources/CMD.csv");
			multipartFile.transferTo(file);
			final File csvFile = new File(path + "/src/main/resources/CMD.csv");
			CSVReader reader = new CSVReaderBuilder(new FileReader(path + "/src/main/resources/CMD.csv"))
					.withSkipLines(1).build();
			Set<Employee> employees = reader.readAll().stream().map(data -> {
				Employee employee = new Employee();
				String name, email, dob, phone, dep, pos, gender, code;
				name = data[0];
				dob = data[1];
				email = data[2];
				phone = data[3];
				dep = data[4];
				pos = data[5];
				gender = data[6];
				code = data[7];
				List<Department> departments = new ArrayList<Department>();
				Department department = new Department();
				department = departmentRepository.findByName(dep);
				List<Position> positions = positionRepository.findAllByDepId(department.getId());
				List<Position> positionsEmp = new ArrayList<Position>();
				String createDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
				for (Position po : positions) {
					if (po.getName().equals(pos)) {
						positionsEmp.add(po);
					}
				}
				if (department != null) {
					departments.add(department);
				}
				employee.setName(name);
				employee.setDateOfBirth(dob);
				employee.setEmail(email);
				employee.setPhoneNumber(phone);
				employee.setDepartments(departments);
				employee.setPositions(positionsEmp);
				employee.setCreateDate(createDate);
				employee.setModifyDate(createDate);
				employee.setCode(code);
				employee.setCreateBy(creatorId);
				employee.setModifyBy(creatorId);
				employee.setActiveFlag(true);
				employee.setActive(true);
				employee.setPassword("cmdcmdcmd");
				employee.setEnableLogin(true);
				employee.setAvatar("https://i.imgur.com/bFbOCtQ.jpg");
				employee.setGender(gender);
				employee.setUsername(email);
				return employee;
			}).collect(Collectors.toSet());

			boolean success = employeeRepository.add(employees);
			

			
			if (success) {
				String messageSuccess = message.getMessageByItemCode("EMPS1");
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", messageSuccess, ""));
			} else {
				String messageError = message.getMessageByItemCode("EMPE3");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", messageError, ""));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in edit()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "", ""));
		}

	}

}
