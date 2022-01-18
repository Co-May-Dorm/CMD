package com.comaymanagement.cmd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.repository.IStatusRepositoty;

@Service
public class StatusService implements IGeneralService<Status> {
	
	@Autowired
	IStatusRepositoty statusRepositoty;

	@Override
	public Iterable<Status> findAll() {
		return statusRepositoty.findAll();
	}

	@Override
	public Optional<Status> findById(String id) {
		return statusRepositoty.findById(id);
	}

	@Override
	public Status save(Status t) {
		return statusRepositoty.save(t);
	}

	@Override
	public void remove(Status model) {
		statusRepositoty.delete(model);
	}
	
}
