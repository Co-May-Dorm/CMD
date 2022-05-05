package com.comaymanagement.cmd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.constant.CMDConstrant;
import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.model.TaskModel;
import com.comaymanagement.cmd.repositoryimpl.TaskRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class TaskService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaskRepositoryImpl taskRepository;

	@Autowired
	Message message;
	public ResponseEntity<Object> findByStatusId(String statusId, String sort, String order, String page) {
		order = order == null ? "DESC" : order;
		sort = sort == null ? "id" : sort;
		page = page == null ? "1" : page;
		Integer limit = CMDConstrant.LIMIT;
		int offset = (Integer.valueOf(page) - 1) * limit;
		try {
			List<TaskModel> tasksByStatusId = taskRepository.findByStatusId(statusId, sort, order, offset, limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(Integer.valueOf(page));
			//pagination.setTotalItem(taskRepository.CountTotalItemTaskAll());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("tasks", tasksByStatusId);
			results.put("pagination", pagination);
			if (tasksByStatusId == null) {
				LOGGER.info("Have no task by status_id: " + statusId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("", "Have no task by status_id: " + statusId, ""));
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully:", results));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", "Have error: ", e.getMessage()));
		}

	}
	
	public ResponseEntity<Object> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, String page) {
		dep = dep == null ? "" : dep.trim();
		title = title == null ? "" : title.trim();
		status = status == null ? "" : status.trim();
		creator = creator == null ? "" : creator.trim();
		receiver = receiver == null ? "" : receiver.trim();
		createDate = createDate == null ? "" : createDate.trim();
		finishDate = finishDate == null ? "" : finishDate.trim();
		order = order == null ? "DESC" : order;
		sort = sort == null ? "id" : sort;
		page = page == null ? "1" : page.trim();
		Integer limit = CMDConstrant.LIMIT;
		// Caculator offset
		int offset = (Integer.parseInt(page) - 1) * limit;
		Set<TaskModel> taskModelSet = new LinkedHashSet<TaskModel>();
		List<TaskModel> taskModelListTMP = new ArrayList<TaskModel>();
		try {
			Integer totalItem = taskRepository.countAllPaging(dep, title, status, creator, receiver, createDate, finishDate, sort, order);
			Integer numberOfItemNeeded = 0;
			numberOfItemNeeded = totalItem < limit ? totalItem : limit; 
			Integer numberDuplicate = numberOfItemNeeded;
			while (taskModelSet.size() < numberOfItemNeeded) {
				numberDuplicate -= taskModelSet.size();  
				offset = taskModelSet.size() == 0 ? offset : (offset + taskModelSet.size() + numberDuplicate);
				limit = numberOfItemNeeded - taskModelSet.size();
				taskModelListTMP = taskRepository.findAll(dep, title, status, creator, receiver, createDate, finishDate, sort, order, offset, limit);
				for(TaskModel taskModel : taskModelListTMP) {
					taskModelSet.add(taskModel);
				}
				taskModelListTMP.clear();
			}
			Integer totalItemEmployee = taskRepository.countAllPaging(dep, title, status, creator, receiver, createDate, finishDate, sort, order);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(Integer.valueOf(page));
			pagination.setTotalItem(totalItemEmployee);
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("tasks", taskModelSet);
			if (results.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", results));
			} else {
				pagination.setPage(1);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", results));
			}
		} catch (Exception e) {
			LOGGER.error("ERROR:" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}

	}


	public ResponseEntity<Object> findByStatusIds(String json, String sort, String order, String page) {
		order = order == null ? "DESC" : order;
		sort = sort == null ? "id" : sort;
		page = page == null ? "1" : page.trim();
		Integer limit = CMDConstrant.LIMIT;
		int offset = (Integer.valueOf(page) - 1) * limit;
		List<TaskModel> tasks = new ArrayList<TaskModel>();
		try {
			
			JsonMapper jsonMapper = new JsonMapper();
			JsonNode jsonObject;
			jsonObject = jsonMapper.readTree(json);
			JsonNode jsonStatusObject = jsonObject.get("statusIds");
			List<Integer> ids = new ArrayList<Integer>();
			for(JsonNode statusId : jsonStatusObject) {
				System.out.println(statusId.toString());
				ids.add(Integer.valueOf(statusId.toString()));
			}
			
			tasks = taskRepository.findByStatusIds(ids, sort, order, offset, limit);

			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(Integer.valueOf(page));
			pagination.setTotalItem(tasks.size());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("tasks", tasks);
			results.put("pagination", pagination);

			if (tasks.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", results));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("Not found", "Can not find task list", results));
			}
		} catch (Exception e) {
			LOGGER.error("ERROR:" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}
	}


	public ResponseEntity<Object> add(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectTask;
		try {
			jsonObjectTask = jsonMapper.readTree(json);

			Task task = new Task();
			
			
			Integer statusId = jsonObjectTask.get("statusId") != null ? jsonObjectTask.get("statusId").asInt() : -1;
			Integer receiverId = jsonObjectTask.get("receiverId") != null ? jsonObjectTask.get("receiverId").asInt() : -1;
			Integer creatorId = jsonObjectTask.get("creatorId") != null ? jsonObjectTask.get("creatorId").asInt() : -1;
			String title = jsonObjectTask.get("title") != null ? jsonObjectTask.get("title").asText() : "";
			String description = jsonObjectTask.get("description") != null ? jsonObjectTask.get("description").asText() : "";
			String createDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
			String finishDate = jsonObjectTask.get("finishDate") != null ? jsonObjectTask.get("finishDate").asText() : " ";
			
			Employee creator = new Employee();
			creator.setId(creatorId);
			
			Employee receiver = new Employee();
			receiver.setId(receiverId);

			Status status = new Status();
			status.setId(statusId);
			
			task.setCreator(creator);
			task.setReceiver(receiver);
			task.setStatus(status);
			task.setTitle(title);
			task.setDescription(description);
			task.setCreateDate(createDate);
			task.setFinishDate(finishDate);
			Integer id = taskRepository.add(task); 
			if ( id != -1) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK",message.getMessageByItemCode("TASKS1"), id));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ResponseObject("ERROR",message.getMessageByItemCode("TASKS1"), ""));
			}

		} catch (Exception e) {
			LOGGER.debug("ERROR",e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("ERROR", e.getMessage() , ""));
		}
	}

	public ResponseEntity<Object> findById(Integer id){
		TaskModel taskModel = new TaskModel();
		taskModel = taskRepository.findById(id);
		
		try {
			if ( id != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Successfully: ", taskModel));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("ERROR", "Have error", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("ERROR",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", "Have error:" , e.getMessage()));

		}
	}
	//
//	public ResponseEntity<Object> sortByStatusIds(Integer statusId,String page) {
//		page = page == null ? "1" : page.trim();
//		List<taskModelAll> tasks = new ArrayList<taskModelAll>();
//		try {
//			tasks = taskRepository.sortByStatusIds(statusId, page);
//			int limit = 15;
//			Pagination pagination = new Pagination();
//			pagination.setLimit(limit);
//			pagination.setPage(Integer.valueOf(page));
//			Map<String, Object> results = new TreeMap<String, Object>();
//			results.put("pagination", pagination);
//			results.put("tasks", tasks);
//			if (results.size() > 0) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("OK", "Query successfully: ", results));
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ResponseObject("Not found", "Can not find task list", tasks));
//			}
//		} catch (Exception e) {
//			LOGGER.error("ERROR:" + e.getMessage());
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
//		}
//
//	}
	// delete task by id
		public ResponseEntity<Object> deleteTaskById(Integer id){
			String updateStatus = taskRepository.deleteTaskById(id);
			try {
				if (updateStatus.equals("1")) {
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", updateStatus + "", ""));
			} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ResponseObject("ERROR", updateStatus + "", ""));

				}
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ResponseObject("ERROR", "Have error:" , e.getMessage()));
				}
			}
		//   
		public ResponseEntity<Object> edit(String json) {
			Task task = new Task();
			JsonMapper jsonMapper = new JsonMapper();
			JsonNode jsonObjectTask;
			String messageCode = "";
			try { 
				jsonObjectTask = jsonMapper.readTree(json);
				Integer id = jsonObjectTask.get("id").asInt();
				Integer statusId = jsonObjectTask.get("statusId") != null ? jsonObjectTask.get("statusId").asInt() : -1;
				Integer receiverId = jsonObjectTask.get("receiverId") != null ? jsonObjectTask.get("receiverId").asInt() : -1;
				Integer creatorId = jsonObjectTask.get("creatorId") != null ? jsonObjectTask.get("creatorId").asInt() : -1;
				String title = jsonObjectTask.get("title") != null ? jsonObjectTask.get("title").asText() : "";
				String description = jsonObjectTask.get("description") != null ? jsonObjectTask.get("description").asText() : "";
				String createDate = jsonObjectTask.get("createDate") != null ? jsonObjectTask.get("createDate").asText() : "";;
				String modifyDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date().getTime());
				String finishDate = jsonObjectTask.get("finishDate") != null ? jsonObjectTask.get("finishDate").asText() : "";
				
				Employee creator = new Employee();
				creator.setId(creatorId);
				
				Employee receiver = new Employee();
				receiver.setId(receiverId);

				Status status = new Status();
				status.setId(statusId);
				// statusId 3 = Đã hủy
				if(statusId==3) {
					messageCode = "TASKS3";
				}else {
					messageCode = "TASKS2";
				}
				task.setId(id);
				task.setCreator(creator);
				task.setReceiver(receiver);
				task.setStatus(status);
				task.setTitle(title);
				task.setDescription(description);
				task.setCreateDate(createDate);
				task.setFinishDate(finishDate);
				Integer messageUpdate = taskRepository.edit(task);
				if (messageUpdate != 0) {
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode(messageCode), task));
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ResponseObject("Error", message.getMessageByItemCode("TASKE3"), task));

				}
			} catch (Exception e) {
				LOGGER.error("Error has occured in edit()", e );
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("ERROR", e.getMessage(), ""));
			}
		}
		//filter
		public ResponseEntity<Object> filter(String createFrom, String createTo, String finishFrom, 
				String finishTo, String title, String creator, String receiver, String dep, String order, String page, String sort) {
			createFrom = createFrom == null ? " " : createFrom.trim();
			createTo = createTo == null ? " " : createTo.trim();
			finishFrom = finishFrom == null ? " " : finishFrom.trim();
			finishTo = finishTo == null ? " " : finishTo.trim();
			dep = dep == null ? " " : dep.trim();
			title = title == null ? " " : title.trim();
			creator = creator == null ? " " : creator.trim();
			receiver = receiver == null ? " " : receiver.trim();
			order = order == null ? "DESC" : order;
			sort = sort == null ? "id" : sort;
			page = page == null ? "1" : page.trim();
			List<TaskModel> tasks = new ArrayList<TaskModel>();
			try {
				int limit = 15;
				tasks = taskRepository.filter(createFrom, createTo, finishFrom, finishTo, title, creator, receiver, dep, limit, order, page, sort);
				Map<String, Object> results = new TreeMap<String, Object>();
				Pagination pagination = new Pagination();
				pagination.setLimit(limit);
				pagination.setPage(Integer.valueOf(page));
				pagination.setTotalItem(taskRepository.countFilter(createFrom, createTo, finishFrom, finishTo, title, creator, receiver, dep));
				results.put("pagination", pagination);
				results.put("tasks", tasks);
				if (results.size() > 0) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ResponseObject("OK", "Query produce successfully ", results));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ResponseObject("Not found ", "Can not find task list ", tasks));
				}
			} catch (Exception e) {
				LOGGER.error("ERROR: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR ", e.getMessage(), ""));
			}

		}

}

