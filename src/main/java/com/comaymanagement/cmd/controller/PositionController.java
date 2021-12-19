package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.PositionService;
@RestController
@RequestMapping("/positions")
public class PositionController {
	@Autowired
	PositionService positionService;
	@GetMapping("")
	public ResponseEntity<Object> findAll() {
		List<CustomPositionAll> customPositions = new ArrayList<>();
		List<Position> positions = positionService.findAll();
		for(Position p : positions) {
			CustomPositionAll custom = new CustomPositionAll();
			custom.setId(p.getId());
			custom.setName(p.getName());
			custom.setIsManager(p.getIsManager());
			custom.setRoleList(p.getRoleList());
			customPositions.add(custom);
		}
			
			if (customPositions.size() > 0 ) {
				return ResponseEntity.status(HttpStatus.OK).body(
						new ResponseObject("OK","Query produce successfully: ", customPositions)
				);
			}else {
				
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject("Not found","Error","")
				);
			}
		}
}
