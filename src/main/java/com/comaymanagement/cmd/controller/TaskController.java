package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.entity.TaskDetail;
import com.comaymanagement.cmd.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	TaskService taskService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable String id){
		
		Optional<Task> task = taskService.findById(id);
		
		
		if (task.get().getId() != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("OK", "Query produce successfully: ", task));
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Can not find task list", ""));
		}
	}

	@GetMapping
	public ResponseEntity<Object> findCustomTaskAlls() {

		List<Task> taskList = (List<Task>) taskService.findAll();
		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();

		for (Task task : taskList) {
			CustomTaskAll customTask = new CustomTaskAll();
			customTask.setCreator(task.getEmployee());
			customTask.setStatus(task.getStatus());

			List<Employee> recieverList = new ArrayList<Employee>();
			for (TaskDetail taskDetail : task.getTaskDetailList()) {
				Employee reciever = taskDetail.getEmployee();
				recieverList.add(reciever);
			}
			customTask.setRecieverList(recieverList);
			customTaskList.add(customTask);
		}

		if (customTaskList.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("OK", "Query produce successfully: ", customTaskList));
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Can not find task list", ""));
		}

	}

	@PostMapping(path = "/insert", produces = "application/json", consumes = "application/json")
	public ResponseEntity<Object> saveTask(@RequestBody Task newTask) {
		
		Task task = new Task();
		task.setEmployee(newTask.getEmployee());
		task.setStatus(newTask.getStatus());

//		Set<TaskDetail> taskDetailList = new HashSet<TaskDetail>();
//		for(TaskDetail taskDetailItem : newTask.getTaskDetailList()) {
//			taskDetailList.add(taskDetailItem);
//		}
//		task.setTaskDetailList(taskDetailList);
		
		if (taskService.save(task).getId()!= null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("OK", "Query produce successfully: ", task));
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Can not find task list", ""));
		}
	}


}
