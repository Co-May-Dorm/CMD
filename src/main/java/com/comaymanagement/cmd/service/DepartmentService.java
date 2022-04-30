package com.comaymanagement.cmd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repositoryimpl.DepartmentRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.MessageRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class DepartmentService {
	@Autowired
	DepartmentRepositoryImpl departmentRepository;
	@Autowired
	PositionRepositoryImpl positionRepository;
	
	@Autowired
	Message message;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

	public ResponseEntity<Object> findAll(String name) {
		name = name == null ? "" : name.trim();
		Set<CustomDepartmentAll> cusDepSet = departmentRepository.findAll(name);
		
		if (cusDepSet.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successful", cusDepSet));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", "Not found", ""));
		}

	}

	public ResponseEntity<Object> add(String json) {
		List<Position> positionList = new ArrayList<>();
		Department dep = new Department();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectPosition;
		try {
			jsonObjectDepartment = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectDepartment.get("positions");
			// Get data
			String code = jsonObjectDepartment.get("code").asText();
			String name = jsonObjectDepartment.get("name") != null ? jsonObjectDepartment.get("name").asText() : "";
			Integer fartherDepartmentId = jsonObjectDepartment.get("fartherDepartmentId") != null ? jsonObjectDepartment.get("fartherDepartmentId").asInt() : -1;
			String description = jsonObjectDepartment.get("description") != null ? jsonObjectDepartment.get("description").asText() : "";
			Integer createBy = jsonObjectDepartment.get("createBy") != null ? jsonObjectDepartment.get("createBy").asInt() : -1;
			String createDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
			Integer modifyBy = -1;
			String modifyDate = "";
			Integer level = jsonObjectDepartment.get("level") != null ? jsonObjectDepartment.get("level").asInt() : -1;
			Integer headPosition = -1;
			
//			Check department code existed
			boolean isExisted = departmentRepository.isExisted(-1, code);

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", message.getMessageByItemCode("DEPE2") , ""));
			}
			
			dep.setCode(code);
			dep.setName(name);
			dep.setFatherDepartmentId(fartherDepartmentId);
			dep.setDescription(description);
			dep.setCreateBy(createBy);
			dep.setCreateDate(createDate);
			dep.setModifyBy(modifyBy);
			dep.setModifyDate(modifyDate);
			dep.setLevel(level);
			dep.setHeadPosition(headPosition);
			// save department..............
			Integer idDepAdded = departmentRepository.save(dep);
			for (JsonNode p : jsonObjectPosition) {
				Role role = new Role();
				Position pos = new Position();
				role.setId(p.get("role").get("id").asInt());
				pos.setName(p.get("name").asText());
				pos.setIsManager(p.get("isManager").asBoolean());
				pos.setCreateBy(createBy);
				pos.setModifyBy(modifyBy);
				pos.setCreateDate(createDate);
				pos.setModifyDate(modifyDate);
				pos.setRole(role);
				pos.setDepartment(dep);
				positionList.add(pos);
			}

			for (Position p : positionList) {
				Integer idAdded = positionRepository.add(p);
				
				if (idAdded == -1) {
					LOGGER.error("Error has occured in DepartmentService at save():");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject("Error", message.getMessageByItemCode("POSE1") , ""));
				}
				if(p.getIsManager()) {
					Department depUpdate = departmentRepository.findById(idDepAdded);
					depUpdate.setHeadPosition(idAdded);
					departmentRepository.edit(depUpdate);
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
			// Get data
			String code = jsonObjectDepartment.get("code").asText();
			String name = jsonObjectDepartment.get("name") != null ? jsonObjectDepartment.get("name").asText() : "";
			Integer fatherDepartmentId = jsonObjectDepartment.get("fatherDepartmentId") != null ? jsonObjectDepartment.get("fatherDepartmentId").asInt() : -1;
			String description = jsonObjectDepartment.get("description") != null ? jsonObjectDepartment.get("description").asText() : "";
			Integer createBy = jsonObjectDepartment.get("createBy") != null ? jsonObjectDepartment.get("createBy").asInt() : -1;
			String createDate = jsonObjectDepartment.get("createDate") != null ? jsonObjectDepartment.get("createDate").asText() : "";
			Integer modifyBy = jsonObjectDepartment.get("modifyBy") != null ? jsonObjectDepartment.get("modifyBy").asInt() : -1;
			String modifyDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
			Integer level = jsonObjectDepartment.get("level") != null ? jsonObjectDepartment.get("level").asInt() : -1;
			Integer headPosition = jsonObjectDepartment.get("headPosition") != null ? jsonObjectDepartment.get("headPosition").asInt() : -1;
//			Check department code existed
			Integer id = jsonObjectDepartment.get("id").asInt();
			boolean isExisted = departmentRepository.isExisted(id, code);
			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("Error", message.getMessageByItemCode("POSE1"), ""));
			}
			dep.setId(id);
			dep.setCode(code);
			dep.setName(name);
			dep.setFatherDepartmentId(fatherDepartmentId);
			dep.setDescription(description);
			dep.setCreateBy(createBy);
			dep.setCreateDate(createDate);
			dep.setModifyBy(modifyBy);
			dep.setModifyDate(modifyDate);
			dep.setLevel(level);
			dep.setHeadPosition(headPosition);
			
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
					pos.setCreateBy(createBy);
					pos.setModifyBy(modifyBy);
					pos.setCreateDate(createDate);
					pos.setModifyDate(modifyDate);
					positionEdits.add(pos);
				}else {
					
					role.setId(p.get("role").get("id").asInt());
					pos.setName(p.get("name").asText());
					pos.setIsManager(p.get("isManager").asBoolean());
					pos.setRole(role);
					pos.setDepartment(dep);
					pos.setCreateBy(createBy);
					pos.setModifyBy(modifyBy);
					pos.setCreateDate(createDate);
					pos.setModifyDate(modifyDate);
					positionAdds.add(pos);
				}
				
				
			}
			// Add position
			for (Position p : positionAdds) {
				Integer idAdded = positionRepository.add(p);
				if (idAdded == -1) {
					LOGGER.error("Error has occured in DepartmentService at edit():");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(new ResponseObject("Error", "Thêm chức vụ vào phòng ban thất bại!", ""));
				}
				if(p.getIsManager()) {
					Department depUpdate = departmentRepository.findById(idDepAdded);
					depUpdate.setHeadPosition(idAdded);
					departmentRepository.edit(depUpdate);
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
				if(p.getIsManager()) {
					Department depUpdate = departmentRepository.findById(idDepAdded);
					depUpdate.setHeadPosition(idAdded);
					departmentRepository.edit(depUpdate);
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
	
	public ResponseEntity<Object> delete(Integer id){
		Department depDelete = departmentRepository.findById(id);
		if(depDelete.getEmployees().size()>0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", message.getMessage(1) , ""));
		}
		String deleteStatus = departmentRepository.delete(id);
		try {
			if (deleteStatus.equals("1")) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", deleteStatus + "", ""));
		} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", deleteStatus + "", ""));

			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", "Have error:" , e.getMessage()));
			}
		}

}
