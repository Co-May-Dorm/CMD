package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class DepartmentService implements IGeneralService<Department> {
	@Autowired
	DepartmentRepositoryImpl departmentRepository;
	@Autowired
	PositionRepositoryImpl positionRepository;
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

	@Override
	public Iterable<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Department> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Department model) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseEntity<Object> save(String json) {
		List<Position> positionList = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectPosition;
		Integer id = -1;
		try {
			jsonObjectDepartment = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectDepartment.get("positions");
//			Check department code existed
			String code = jsonObjectDepartment.get("code").asText();
			boolean isExisted = departmentRepository.isExisted(id, code);

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", "Mã phòng ban này đã tồn tại!", ""));
			}
			dep.setCode(code);
			dep.setName(jsonObjectDepartment.get("name").asText());
			dep.setFatherDepartmentId(jsonObjectDepartment.get("fatherDepartmentId").asInt());
			dep.setDescription(jsonObjectDepartment.get("description").asText());
			// save department..............
			Integer idDepAdded = departmentRepository.save(dep);
			int i = 1;
			for (JsonNode p : jsonObjectPosition) {
				Role role = new Role();
				Position pos = new Position();
				role.setId(p.get("role").get("id").asInt());
				pos.setCode(dep.getCode() + i);
				pos.setName(p.get("name").asText());
				pos.setIsManager(p.get("isManager").asBoolean());
				pos.setRole(role);
				pos.setDepartment(dep);
				positionList.add(pos);
				i++;
			}

			for (Position p : positionList) {
				Integer idAdded = positionRepository.save(p);
				if (idAdded == -1) {
					LOGGER.error("Error has occured in DepartmentService at save():");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject("Error", "Thêm chức vụ vào phòng ban thất bại!", ""));
				}
			}
			if (idDepAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idDepAdded + "", dep));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "", dep));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentService at add() ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));

		}
	}
	
	public ResponseEntity<Object> edit(String json) {
		List<Position> positionEdits = new ArrayList<>();
		List<Position> positionAdds = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectPosition;
		
		try {
			jsonObjectDepartment = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectDepartment.get("positions");
//			Check department code existed
			Integer id = jsonObjectDepartment.get("id") != null ? jsonObjectDepartment.get("id").asInt() : -1;
			String code = jsonObjectDepartment.get("code").asText();
			boolean isExisted = departmentRepository.isExisted(id, code);

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", "Mã phòng ban này đã tồn tại!", ""));
			}
			dep.setId(id);
			dep.setCode(code);
			dep.setName(jsonObjectDepartment.get("name").asText());
			dep.setFatherDepartmentId(jsonObjectDepartment.get("fatherDepartmentId").asInt());
			dep.setDescription(jsonObjectDepartment.get("description").asText());
			// save department..............
			Integer idDepAdded = departmentRepository.edit(dep);
			for (JsonNode p : jsonObjectPosition) {
				Role role = new Role();
				Position pos = new Position();
				// If don't have id => go to save, else => go to edit
				Integer posId = p.get("id") != null ? p.get("id").asInt() : -1;
				if(posId != -1) {
					role.setId(p.get("role").get("id").asInt());
					pos.setId(posId);
					pos.setName(p.get("name").asText());
					pos.setIsManager(p.get("isManager").asBoolean());
					pos.setRole(role);
					pos.setDepartment(dep);
					positionEdits.add(pos);
				}else {
					
					role.setId(p.get("role").get("id").asInt());
					pos.setName(p.get("name").asText());
					pos.setIsManager(p.get("isManager").asBoolean());
					pos.setRole(role);
					pos.setDepartment(dep);
					positionAdds.add(pos);
				}
				
				
			}
			// Add position
			for (Position p : positionAdds) {
				Integer idAdded = positionRepository.save(p);
				if (idAdded == -1) {
					LOGGER.error("Error has occured in DepartmentService at edit():");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject("Error", "Thêm chức vụ vào phòng ban thất bại!", ""));
				}
			}
			// Edit position
			for (Position p : positionEdits) {
				Integer idAdded = positionRepository.edit(p);
				if (idAdded == -1) {
					LOGGER.error("Error has occured in DepartmentService at edit():");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject("Error", "Thêm chức vụ vào phòng ban thất bại!", ""));
				}
			}
			
			if (idDepAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idDepAdded + "", dep));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "", dep));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentService at add() ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));

		}
	}
	@Override
	public ResponseEntity<Object> save(Department t) {
		// TODO Auto-generated method stub
		return null;
	}

}
