package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.service.PositionService;

@RestController
@RequestMapping("/positions")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class PositionController {
	@Autowired
	PositionService positionService;

	@GetMapping("/{roleId}")
	public ResponseEntity<Object> findAll(@PathVariable(value = "roleId", required = true) String roleId) {
		return positionService.findAllByRoleId(roleId);
	}
}
