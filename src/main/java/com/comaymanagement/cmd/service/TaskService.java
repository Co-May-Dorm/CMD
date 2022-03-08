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

import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repository.ITaskRepository;
import com.comaymanagement.cmd.repositoryimpl.TaskRepositoryImpl;

@Service
public class TaskService implements IGeneralService<Task> {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaskRepositoryImpl taskRepository;

	public ResponseEntity<Object> findByStatusId(String statusId, String sort, String order, Integer limit, String page) {
		try {

			List<CustomTaskAll> taskListByStatusId = taskRepository.findByStatusId(statusId,sort,order,limit,page);
			

			if(taskListByStatusId == null) {
				LOGGER.info("Have no task by status_id: " + statusId );
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("","Have no task by status_id: " + statusId,""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",taskListByStatusId));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}

	}

	public ResponseEntity<Object> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, Integer limit, String page) {

		List<CustomTaskAll> taskList = new ArrayList<CustomTaskAll>();
		try {

			taskList = taskRepository.findAllTask(dep, title, status, creator, receiver, createDate,
					finishDate, sort, order, limit, page);

			if (taskList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", taskList));
			} else {

				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("Not found", "Can not find task list", ""));
			}
		} catch (Exception e) {
			LOGGER.error("ERROR:" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}

	}

	@Override
	public Iterable<Task> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Task> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task save(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Task model) {
		// TODO Auto-generated method stub

	}

}
