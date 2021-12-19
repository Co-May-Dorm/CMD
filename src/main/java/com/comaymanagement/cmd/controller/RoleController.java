package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.customentity.CustomRoleAll;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {
	@Autowired
	RoleService roleService;
	@GetMapping("")
	public ResponseEntity<Object> findAll(){
		List<CustomRoleAll> customRoles = new ArrayList<>();
		List<Role> roles = roleService.findAll();
		for(Role r : roles) {
			CustomRoleAll custom = new CustomRoleAll();
			custom.setId(r.getId());
			custom.setName(r.getName());
			custom.setAuthList(r.getAuthList());
			custom.setPosition(r.getPosition());
			custom.setRoleDetailList(r.getRoleDetailList());
			customRoles.add(custom);
		}
		
		if(customRoles.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(
					 new ResponseObject("OK","Success successfully", customRoles)
					
			);
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(
					 new ResponseObject("OK","Success successfully", "")
					
			);
		}
		
		
	}
}
