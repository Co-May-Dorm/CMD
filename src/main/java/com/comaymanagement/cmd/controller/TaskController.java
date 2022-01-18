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

		if (task != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("OK", "Query produce successfully: ", task));
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("Not found", "Can not find task list", ""));
		}
	}

	@GetMapping(value= "",produces = "application/json")
	public ResponseEntity<Object> findCustomTaskAlls(				
			@RequestParam(value="page",required = true) String page, 
			@RequestParam(value="department",required = false) String dep, 
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
		
		dep = dep == null ? "":dep;
		title = title == null ? "" : title;
		status = status == null ? "" : status;
		creator = creator == null ? "" : creator;
		receiver = receiver == null ? "": receiver;
		createDate = createDate == null ? "" : createDate;
		finishDate = finishDate == null ? "" : finishDate;
		sort = sort == null ? "" : sort;
		order = order == null ? "" : order;
		limit = limit == null ? 10 : limit;
		sort = sort == null ? "" : sort;
		page = (dep != "" || title != "" || status != "" || creator != "" || 
				receiver != "" || createDate != "" || finishDate != "") ? "1" : page;
		
		try {
			int offset = (Integer.valueOf(page) - 1)*limit;
			
			List<Task> taskList = (List<Task>) taskService.findAll();
			List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
			taskList = taskService.findAllTask(dep,title,status,creator,receiver,createDate,finishDate,sort,order,limit,offset);
			
			for(Task task : taskList) {
				CustomTaskAll customTask = new CustomTaskAll();
				customTask.setTaskId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(task.getCreator().getDepartmentId().getName());
				customTask.setStatusName(task.getStatus().getName());
				
				customTaskList.add(customTask);
			}
			if (customTaskList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", customTaskList));
			} else {

				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("Not found", "Can not find task list", ""));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", e.getMessage(), ""));
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

	}
	
	//Get task list by status id 
	@GetMapping("/status/{id}")
	public ResponseEntity<Object> findByStatus(@PathVariable String id){
		LOGGER.info("Get task list by status");
		try {
			List<Task> taskListByStatusId = taskService.findByStatusId(id);
			if(taskListByStatusId == null) {
				LOGGER.info("Have no task by status_id: " + id );
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("","Have no task by status_id: " + id,""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",taskListByStatusId));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}


	}

}
