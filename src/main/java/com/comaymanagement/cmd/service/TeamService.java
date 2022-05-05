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
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.model.TeamModel;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.comaymanagement.cmd.repositoryimpl.TeamRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
@Service
@Transactional(rollbackFor = Exception.class)
public class TeamService {
	@Autowired
	TeamRepositoryImpl teamRepository;
	@Autowired
	PositionRepositoryImpl positionRepository;
	
	@Autowired
	Message message;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

	public ResponseEntity<Object> findAll(String name) {
		name = name == null ? "" : name.trim();
		Set<TeamModel> teamModelSet = teamRepository.findAll(name);
		
		if (teamModelSet.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Successful", teamModelSet));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", "Not found", ""));
		}

	}

	public ResponseEntity<Object> add(String json) {
		List<Position> positionList = new ArrayList<>();
		Team team = new Team();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectPosition;
		try {
			jsonObjectDepartment = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectDepartment.get("positions");
			// Get data
			String code = jsonObjectDepartment.get("code").asText();
			String name = jsonObjectDepartment.get("name") != null ? jsonObjectDepartment.get("name").asText() : "";
			String description = jsonObjectDepartment.get("description") != null ? jsonObjectDepartment.get("description").asText() : "";
			Integer createBy = jsonObjectDepartment.get("createBy") != null ? jsonObjectDepartment.get("createBy").asInt() : -1;
			String createDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
			Integer modifyBy = -1;
			String modifyDate = "";
			Integer headPosition = -1;
			
//			Check department code existed
			boolean isExisted = teamRepository.isExisted(-1, code);

			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ERROR", message.getMessageByItemCode("TEAME1") , ""));
			}
			
			team.setCode(code);
			team.setName(name);
			team.setDescription(description);
			team.setCreateBy(createBy);
			team.setCreateDate(createDate);
			team.setModifyBy(modifyBy);
			team.setModifyDate(modifyDate);
			team.setHeadPosition(headPosition);
			// save team..............
			Integer idTeamAdded = teamRepository.add(team);
			Team teamUpdate = teamRepository.findById(idTeamAdded);
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
				pos.setTeam(team);
				positionList.add(pos);
			}

			for (Position p : positionList) {
				Integer idAdded = positionRepository.add(p);
				if (idAdded == -1) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ResponseObject("ERROR", message.getMessageByItemCode("POSE1") , ""));
				}
				if(p.getIsManager()) {
					teamUpdate.setHeadPosition(idAdded);
					teamRepository.edit(teamUpdate);
				}
			}
			
			if (idTeamAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("TEAME4"), teamUpdate));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("ERROR",message.getMessageByItemCode("TEAME2"), teamUpdate));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at add() ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("ERROR", e.getMessage(), ""));

		}
	}
	
	public ResponseEntity<Object> edit(String json) {
		List<Position> positionEdits = new ArrayList<>();
		List<Position> positionAdds = new ArrayList<>();
		Team team = new Team();
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectDepartment;
		JsonNode jsonObjectPosition;
		
		try {
			jsonObjectDepartment = jsonMapper.readTree(json);
			jsonObjectPosition = jsonObjectDepartment.get("positions");
			// Get data
			String code = jsonObjectDepartment.get("code").asText();
			String name = jsonObjectDepartment.get("name") != null ? jsonObjectDepartment.get("name").asText() : "";
			String description = jsonObjectDepartment.get("description") != null ? jsonObjectDepartment.get("description").asText() : "";
			Integer createBy = jsonObjectDepartment.get("createBy") != null ? jsonObjectDepartment.get("createBy").asInt() : -1;
			String createDate = jsonObjectDepartment.get("createDate") != null ? jsonObjectDepartment.get("createDate").asText() : "";
			Integer modifyBy = jsonObjectDepartment.get("modifyBy") != null ? jsonObjectDepartment.get("modifyBy").asInt() : -1;
			String modifyDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
			Integer headPosition = -1;
//			Check department code existed
			Integer id = jsonObjectDepartment.get("id").asInt();
			boolean isExisted = teamRepository.isExisted(id, code);
			if (isExisted) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ERROR", message.getMessageByItemCode("TEAME3"), ""));
			}
			team.setId(id);
			team.setCode(code);
			team.setName(name);
			team.setDescription(description);
			team.setCreateBy(createBy);
			team.setCreateDate(createDate);
			team.setModifyBy(modifyBy);
			team.setModifyDate(modifyDate);
			team.setHeadPosition(headPosition);
			Team teamUpdate = teamRepository.findById(id);
			// save department..............
			Integer messageEdit = teamRepository.edit(team);
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
					pos.setTeam(team);
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
					pos.setTeam(team);
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
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ResponseObject("Error",message.getMessageByItemCode("POSE1"), ""));
				}
				else if(p.getIsManager()) {
					teamUpdate = teamRepository.findById(id);
					teamUpdate.setHeadPosition(idAdded);
					teamRepository.edit(teamUpdate);
				}
			}
			// Edit position
			for (Position p : positionEdits) {
				Integer idAdded = positionRepository.edit(p);
				if (idAdded == -1) {
					LOGGER.error("Error has occured at edit():");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ResponseObject("ERROR", message.getMessageByItemCode("POSE2"), ""));
				}
				else if (p.getIsManager()) {
					teamUpdate.setHeadPosition(p.getId());
					teamRepository.edit(teamUpdate);
				}
			}
			
			if (messageEdit != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("TEAME5"), teamUpdate));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("Error", "", teamUpdate));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at add() ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("Error", e.getMessage(), ""));

		}
	}
	
	public ResponseEntity<Object> delete(Integer id){
		Team teamDelete = teamRepository.findById(id);
		if(teamDelete.getEmployees().size()>0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR",message.getMessageByItemCode("TEAME2") , ""));
		}
		String deleteStatus = teamRepository.delete(id);
		try {
			if (deleteStatus.equals("1")) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("TEAME6"), ""));
		} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", deleteStatus + "", ""));

			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", e.getMessage(),""));
			}
		}

}
