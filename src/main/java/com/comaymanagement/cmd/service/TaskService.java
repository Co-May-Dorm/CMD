package com.comaymanagement.cmd.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repository.ITaskRepository;

@Service
public class TaskService implements IGeneralService<Task> {

	@Autowired
	ITaskRepository taskRepository;
	
	@Override
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}


	@Override
	public Task save(Task t) {
		// TODO Auto-generated method stub
		return taskRepository.save(t);
	}

	@Override
	public void remove(Task model) {
		taskRepository.delete(model);
		
	}

	@Override
	public Optional<Task> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
