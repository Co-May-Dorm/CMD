package com.comaymanagement.cmd.service;

import java.util.List;
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
		return taskRepository.save(t);
	}

	@Override
	public void remove(Task model) {
		taskRepository.delete(model);

	}

	@Override
	public Optional<Task> findById(String id) {
		return taskRepository.findById(id);
	}
	
	public List<Task> findByStatusId(String id){
		return taskRepository.findByStatusId(id);
	}
	public List<Task> findAllTask( String dep, 
			 String title, 
			 String status, 
			 String creator, 
			 String receiver,
			 String createDate,
			 String finishDate,
			 String sort,
			 String order,
			 Integer limit,
			 Integer offset){
		return taskRepository.findAllTask(dep,title,status,creator,receiver,createDate,finishDate,sort,order,limit,offset);
	}

}
