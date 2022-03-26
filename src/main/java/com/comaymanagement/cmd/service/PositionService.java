package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.repository.IPositionRepository;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class PositionService implements IGeneralService<Position> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PositionRepositoryImpl positionRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<Object> findAllByRoleId(Integer roleId) {
		List<CustomPositionAll> customPositionAlls = new ArrayList<CustomPositionAll>();;

		try {
			if (roleId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("ERROR", "Have error: ", "Role ID is null"));
			} else {
				customPositionAlls = positionRepository.findAllByRoleId(roleId);
				if (customPositionAlls.size() < 1) {
					LOGGER.info("Have no task by status_id: " + roleId);
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject("", "Have no task by status_id: " + roleId, ""));
				} else {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject("OK", "Query produce successfully:", customPositionAlls));
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", "Have error: ", e.getMessage()));
		}
	}
	public ResponseEntity<Object> findAllByDepartmentId(Integer depId) {
		List<CustomPositionAll> customPositionAlls = new ArrayList<CustomPositionAll>();;

		try {
			if (depId == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("ERROR", "Have error: ", "Department ID is null"));
			} else {
				customPositionAlls = positionRepository.findAllByDepartmentId(depId);
				if (customPositionAlls.size() < 1) {
					LOGGER.info("Have no task by status_id: " + depId);
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject("", "Have no task by status_id: " + depId, ""));
				} else {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject("OK", "Query produce successfully:", customPositionAlls));
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", "Have error: ", e.getMessage()));
		}
	}
	@Override
	public Optional<Position> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> save(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectPosition;
		Position p = new Position();
		Department dep = new Department();
		Role role = new Role();
		Team team = new Team();
		Integer id = -1;
		try {
			jsonObjectPosition = jsonMapper.readTree(json);
			p.setCode(jsonObjectPosition.get("code").asText());
			p.setName(jsonObjectPosition.get("name").asText());
			p.setIsManager(jsonObjectPosition.get("isManager").asBoolean());
			team.setId(jsonObjectPosition.get("teamId").asInt());
			dep.setId(jsonObjectPosition.get("departmentId").asInt());
			role.setId(jsonObjectPosition.get("roleId").asInt());
			p.setTeam(team);
			p.setDepartment(dep);
			p.setRole(role);
			Integer idAdded = positionRepository.save(p);
			if (idAdded != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idAdded + "", "employee" + p));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("Error", idAdded + "", p));
			}
		} catch (Exception e) {
			logger.error("Error has occured in PositionService at save()", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("Error", e.getMessage(), ""));
		}
	}
	
	@Override
	public ResponseEntity<Object> save(Position p) {
		Integer idAdded = positionRepository.save(p);
		if (idAdded != -1) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", idAdded + "", "employee" + p));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("Error", idAdded + "", p));
		}
	}
	@Override
	public void remove(Position model) {
		// TODO Auto-generated method stub

	}


	public Iterable<Position> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
