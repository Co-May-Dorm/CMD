package com.comaymanagement.cmd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.repository.IStatusRepositoty;
import com.comaymanagement.cmd.repositoryimpl.StatusRepositotyImpl;

@Service
public class StatusService implements IGeneralService<Status> {
	
	@Autowired
	StatusRepositotyImpl statusRepositoty;

	@Override
	public Iterable<Status> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Status> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Object> save(Status t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Status model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResponseEntity<Object> save(String json) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
