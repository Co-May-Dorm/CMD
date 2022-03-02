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
import org.springframework.web.bind.annotation.RequestBody;
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
import com.comaymanagement.cmd.service.EmployeeService;
import com.comaymanagement.cmd.service.StatusService;
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
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	StatusService statusService;
	

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
			@RequestParam(value="page",required = false) String page, 
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
		LOGGER.info("Get task list");
		dep = dep == null ? " ":dep;
		title = title == null ? " " : title;
		status = status == null ? " " : status;
		creator = creator == null ? " " : creator;
		receiver = receiver == null ? " ": receiver;
		createDate = createDate == null ? " " : createDate;
		finishDate = finishDate == null ? " " : finishDate;
		sort = sort == null ? " " : sort;
		order = order == null ? "t.unique_number" : order;
		limit = limit == null ? 10 : limit;
		sort = sort == null ? "DESC" : sort;
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
				customTask.setDepartmentName(task.getCreator().getDepartment().getName());
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
			LOGGER.error("ERROR:" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", e.getMessage(), ""));
		}
	}

	@PostMapping("/insert")
	@ResponseBody
	public ResponseEntity<Object> saveTask(@RequestBody String json) {

		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectSanPham;
		try {
			jsonObjectSanPham = jsonMapper.readTree(json);

			Task task = new Task();
			
			String creatorId = jsonObjectSanPham.get("creator_id").asText();
			String receiverId = jsonObjectSanPham.get("receiver_id").asText();
			String statusId = jsonObjectSanPham.get("status_id").asText();
			
			Optional<Employee> creator = employeeService.findById(creatorId);
			Optional<Employee> receiver = employeeService.findById(receiverId);
			Optional<Status> status = statusService.findById(statusId);
			
			task.setId(jsonObjectSanPham.get("id").asText());
			task.setCreator(creator.get());
			task.setReceiver(receiver.get());
			task.setStatus(status.get());
			task.setTitle(jsonObjectSanPham.get("title").asText());
			task.setDescription(jsonObjectSanPham.get("description").asText());
			task.setCreateDate(jsonObjectSanPham.get("createDate").asText());
			task.setFinishDate(jsonObjectSanPham.get("finishDate").asText());

			if (taskService.save(task) != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", task));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("ERROR", "Can not save task", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("ERROR",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", "Have error:" , e.getMessage()));
		}

	}
	
	//Get task list by status id 
	@GetMapping(value="/status/{statusId}",produces = "application/json")
	public ResponseEntity<Object> findByStatus(@PathVariable String statusId,
			@RequestParam(value="page",required = false) String page, 
			@RequestParam(value="sort", required = false) String sort,
			@RequestParam(value="order", required = false) String order,
			@RequestParam(value="limit", required = false) Integer limit){
		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
		LOGGER.info("Get task list by status");
		
		sort = sort == null ? " " : sort;
		order = order == null ? "t.unique_number" : order;
		limit = limit == null ? 10 : limit;
		sort = sort == null ? "DESC" : sort;
		page = page == null ? "1" : page;
		try {
			int offset = (Integer.valueOf(page) - 1) * limit;
			List<Task> taskListByStatusId = taskService.findByStatusId(statusId,sort,order,limit,offset);
			
			for(Task task : taskListByStatusId) {
				CustomTaskAll customTask = new CustomTaskAll();
				customTask.setTaskId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(task.getCreator().getDepartment().getName());
				customTask.setStatusName(task.getStatus().getName());
				
				customTaskList.add(customTask);
			}
			if(taskListByStatusId == null) {
				LOGGER.info("Have no task by status_id: " + statusId );
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("","Have no task by status_id: " + statusId,""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",customTaskList));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}


	}

}
