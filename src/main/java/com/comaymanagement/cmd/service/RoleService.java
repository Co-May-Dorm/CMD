package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repository.IRoleRepository;
import com.comaymanagement.cmd.repositoryimpl.RoleRepositoryImpl;

@Service
public class RoleService implements IGeneralService<Role> {
	@Autowired
	RoleRepositoryImpl roleRepository;


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
