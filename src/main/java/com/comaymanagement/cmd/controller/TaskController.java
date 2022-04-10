package com.comaymanagement.cmd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.service.EmployeeService;
import com.comaymanagement.cmd.service.StatusService;
import com.comaymanagement.cmd.service.TaskService;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = {CrossOriginConstant.REACT_ORIGIN,CrossOriginConstant.REACT_ORIGIN_LOCAL})
public class TaskController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TaskService taskService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	StatusService statusService;
	
	/*
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable String id) {

		Optional<Task> task = taskService.findById(id);

		if (task != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("OK", "Query produce successfully: ", task));
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Can not find task list", ""));
		}
	}*/

	@GetMapping(value= "",produces = "application/json")
	public ResponseEntity<Object> findAll(				
			@RequestParam(value="page",required = false) String page, 
			@RequestParam(value="dep",required = false) String dep, 
			@RequestParam(value="title",required = false) String title, 
			@RequestParam(value="status", required = false) String status,  
			@RequestParam(value="creator", required = false) String creator, 
			@RequestParam(value="receiver", required = false) String receiver, 
			@RequestParam(value="dateCreated", required = false) String createDate,
			@RequestParam(value="dateFinish", required = false) String finishDate,
			@RequestParam(value="sort", required = false) String sort,
			@RequestParam(value="order", required = false) String order,
			@RequestParam(value="limit", required = false) Integer limit
			) {
		LOGGER.info("Get task list");
		return taskService.findAllTask(dep,title,status,creator,receiver,createDate,finishDate,sort,order,page);
	}
	
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<Object> save(@RequestBody String json) {
		return taskService.save(json);
	}

	//Get task list by status id 
	@GetMapping(value="/status/{statusId}",produces = "application/json")
	public ResponseEntity<Object> findByStatusId(@PathVariable String statusId,
			@RequestParam(value="page",required = false) String page, 
			@RequestParam(value="sort", required = false) String sort,
			@RequestParam(value="order", required = false) String order){
		LOGGER.info("Get task list by status");
		return taskService.findByStatusId(statusId, sort, order, page);
	}
	
	//Get task list by status ids 
	@PostMapping(value="/status",produces = "application/json")
	public ResponseEntity<Object> findByStatusIds(
			@RequestBody String json,
			@RequestParam(value="page",required = false) String page, 
			@RequestParam(value="sort", required = false) String sort,
			@RequestParam(value="order", required = false) String order){
		LOGGER.info("Get task list by status ids");
		return taskService.findByStatusIds(json, sort, order, page);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> findById(@PathVariable Integer id){

		return taskService.findById(id);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Object> deleteTaskById(@PathVariable Integer id){
		
			return taskService.deleteTaskById(id);
	}

	@PutMapping(value = "/edit")
	@ResponseBody
	public ResponseEntity<Object> editTask(@RequestBody String json) {
		return taskService.edit(json);
	}
}
