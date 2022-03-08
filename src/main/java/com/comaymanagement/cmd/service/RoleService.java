package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomRoleAll;
import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repository.IRoleRepository;
import com.comaymanagement.cmd.repositoryimpl.RoleRepositoryImpl;

@Service
public class RoleService implements IGeneralService<Role> {
	@Autowired
	RoleRepositoryImpl roleRepository;
	
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<Object> findAllRole(String sort, String order, String page) {
		try {

			List<CustomRoleAll> customRoleAlls = roleRepository.findAllRole(sort, order, page);

			if(customRoleAlls == null) {
				LOGGER.info("NOT FOUND");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Have error:","NOT FOUND",""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",customRoleAlls));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}

	}

	@Override
	public Optional<Role> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role save(Role t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Role model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Role> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
