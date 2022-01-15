package com.comaymanagement.cmd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.service.TaskService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = CrossOriginConstant.REACT_ORIGIN)
public class TaskController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TaskService taskService;

	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable String id) {

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
			customTask.setCreator(task.getCreator());
			customTask.setStatus(task.getStatus());

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

	@PostMapping("/insert")
	@ResponseBody
	public ResponseEntity<Object> saveTask(@RequestParam String json) {

		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectSanPham;
		try {
			jsonObjectSanPham = jsonMapper.readTree(json);
			JsonNode creator = jsonObjectSanPham.get("creator");

			Task task = new Task();

			task.setId(jsonObjectSanPham.get("id").asText());

			Employee creatorDetails = new Employee();
			creatorDetails.setId(creator.get("id").asText());
			creatorDetails.setName(creator.get("name").asText());
			creatorDetails.setDateOfBirth(creator.get("dateOfBirth").asText());
			creatorDetails.setEmail(creator.get("email").asText());
			creatorDetails.setPhoneNumber(creator.get("phoneNumber").asText());
			creatorDetails.setActiveFlag(creator.get("activeFlag").asBoolean());
			task.setCreator(creatorDetails);

			JsonNode status = jsonObjectSanPham.get("status");
			Status statusDetails = new Status();
			statusDetails.setId(status.get("id").asText());
			statusDetails.setName(status.get("name").asText());

			task.setStatus(statusDetails);

			if (taskService.save(task).getId() != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", task));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("ERROR", "Can not save task", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("ERROR",e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("ERROR", "Can not save task", ""));
			
		}

//		Set<TaskDetail> taskDetailList = new HashSet<TaskDetail>();
//		for(TaskDetail taskDetailItem : newTask.getTaskDetailList()) {
//			taskDetailList.add(taskDetailItem);
//		}
//		task.setTaskDetailList(taskDetailList);

	}

}
