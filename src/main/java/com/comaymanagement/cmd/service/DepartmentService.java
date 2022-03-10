package com.comaymanagement.cmd.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;

@Service
public class DepartmentService {
	@Autowired
	DepartmentRepositoryImpl departmentRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

	public List<Department> findAllDepartmentByEmployeeId(String id) {
		return departmentRepository.findAllDepartmentByEmployeeId(id);
	}

	public ResponseEntity<Object> findAll(String name) {
		name = name == null ? "" : name.trim();
		List<CustomDepartmentAll> cusDepList = departmentRepository.findAll(name);

		if (cusDepList.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successful", cusDepList));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "Not found", ""));
		}

	}

//	public ResponseEntity<Object> add(String json){
//		List<Position> positionList = new ArrayList<>();
//		Department dep = new Department();
//		JsonMapper jsonMapper = new JsonMapper();
//		JsonNode jsonObjectDepartment;
//		JsonNode jsonObjectPosition;
//		JsonNode jsonObjectRole;
//		String id = -1;
//		try {
//			jsonObjectDepartment = jsonMapper.readTree(json);
//			jsonObjectPosition = jsonObjectDepartment.get("positionList");
//			JsonNode tmp = jsonObjectDepartment.get("uniqueNumber");
//			if (tmp != null) {
//				id = jsonObjectDepartment.get("uniqueNumber").asInt();
//			}
////			emp = employeeRepository.load(uniqueNumber);
////			Check employee id existed
//			boolean isExisted = departmentRepository.isExisted(id,
//					jsonObjectDepartment.get("id").asText());
//
//			if (isExisted) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("Error", "Mã phòng ban này đã tồn tại!", ""));
//			}
//			// get position list
//			for(JsonNode p : jsonObjectPosition) {
//				Role role = new Role();
//				Position pos = new Position();
//				role.setId(p.get("role").get("id").asText());
//				pos.setId(p.get("id").asText());
//				pos.setName(p.get("name").asText());
//				pos.setIsManager(p.get("isManager").asBoolean());
//				pos.setRole(role);
//				positionList.add(pos);
//			}
//			dep.setId(jsonObjectDepartment.get("id").asText());
//			dep.setName(jsonObjectDepartment.get("name").asText());
//			dep.setFatherDepartmentId(jsonObjectDepartment.get("fatherDepartmentId").asText());
//			dep.setManagerId(jsonObjectDepartment.get("managerId").asText());
//			//save department..............
//			departmentRepository.add(dep);
////			int count = //Count list pos;
////			for(Position p : positionList) {
////				Position pos = new Position();
////				pos.setId();
////				pos.setDepartment(dep);
////				//save possition.....
////				count++;
////			}
//		} catch (Exception e) {
//			LOGGER.error("Error has occured in DepartmentService at add() ", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
//
//		}
//		// (If uniqueNumber == -1) => add, else => edit
//		if (id == -1) {
//			String message = departmentRepository.add(dep);
//			if (message != "") {
//				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message + "", dep));
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body(new ResponseObject("Error", message + "", dep));
//
//			}
//		} else {
//			// update status: 1: successful, 0: fail
//			dep.setId(id);
//			String updateStatus = departmentRepository.edit(dep);
//			if (updateStatus != "") {
//				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", updateStatus + "", dep));
//			} else {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body(new ResponseObject("Error", updateStatus + "", dep));
//
//			}
//		}
//	}

}
