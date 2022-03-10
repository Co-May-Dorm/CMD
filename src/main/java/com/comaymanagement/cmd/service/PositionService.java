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
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.repository.IPositionRepository;
import com.comaymanagement.cmd.repositoryimpl.PositionRepositoryImpl;

@Service
public class PositionService implements IGeneralService<Position> {
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

	@Override
	public Optional<Position> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position save(Position t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Position> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
