package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repositoryimpl.TaskRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
public class TaskService implements IGeneralService<CustomTaskAll> {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaskRepositoryImpl taskRepository;

	public ResponseEntity<Object> findByStatusId(String statusId, String sort, String order, String page) {
		order = order == null ? "DESC" : order;
		sort = sort == null ? "id" : sort;
		page = page == null ? "1" : page;
		try {
			int limit = 15;
			List<CustomTaskAll> tasksByStatusId = taskRepository.findByStatusId(statusId, sort, order, page, limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
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
		List<CustomTaskAll> tasks = new ArrayList<CustomTaskAll>();
		try {
			int limit = 15;
			tasks = taskRepository.findAll(dep, title, status, creator, receiver, createDate, finishDate, sort,
					order, page, limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.countAll(dep, title, status, creator, receiver));
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("tasks", tasks);
			if (results.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", results));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("Not found", "Can not find task list", tasks));
			}
		} catch (Exception e) {
			LOGGER.error("ERROR:" + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}

	}

	@SuppressWarnings("unused")
	public ResponseEntity<Object> findByStatusIds(String json, String sort, String order, String page) {
		order = order == null ? "DESC" : order;
		sort = sort == null ? "id" : sort;
		page = page == null ? "1" : page.trim();
		List<CustomTaskAll> tasks = new ArrayList<CustomTaskAll>();
		try {
			int limit = 15;

			JsonMapper jsonMapper = new JsonMapper();
			JsonNode jsonObject;
			jsonObject = jsonMapper.readTree(json);
			List<String> ids = jsonObject.get("statusIds").findValuesAsText("Id");
			tasks = taskRepository.findByStatusIds(ids, sort, order, page, limit);

			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.countFindByIds());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("tasks", tasks);

			if (tasks.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", results));
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
	public Iterable<CustomTaskAll> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CustomTaskAll> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void remove(CustomTaskAll model) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseEntity<Object> save(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectSanPham;
		try {
			jsonObjectSanPham = jsonMapper.readTree(json);

			Task task = new Task();
			
			String creatorId = jsonObjectSanPham.get("creator_id").asText();
			String receiverId = jsonObjectSanPham.get("receiver_id").asText();
			String statusId = jsonObjectSanPham.get("status_id").asText();
			String code = jsonObjectSanPham.get("code").asText();
			
			Employee creator = new Employee();
			creator.setId(Integer.parseInt(creatorId));
			
			Employee receiver = new Employee();
			receiver.setId(Integer.parseInt(receiverId));

			Status status = new Status();
			status.setId(Integer.parseInt(statusId));
			
			task.setCode(code + taskRepository.getMaxId());
			task.setCreator(creator);
			task.setReceiver(receiver);
			task.setStatus(status);
			task.setTitle(jsonObjectSanPham.get("title").asText());
			task.setDescription(jsonObjectSanPham.get("description").asText());
			task.setCreateDate(jsonObjectSanPham.get("createDate").asText());
			task.setFinishDate(jsonObjectSanPham.get("finishDate").asText());
			Integer id = taskRepository.save(task); 
			if ( id != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", id));
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

	@Override
	public ResponseEntity<Object> save(CustomTaskAll t) {
		// TODO Auto-generated method stub
		return null;
	}
	public ResponseEntity<Object> findById(Integer id){
		CustomTaskAll customTask = new CustomTaskAll();
		customTask = taskRepository.findById(id);
		
		try {
			if ( id != null) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Successfully: ", customTask));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ResponseObject("ERROR", "Can not save task", ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.debug("ERROR",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", "Have error:" , e.getMessage()));
		
//		
//	return ResponseEntity.status(HttpStatus.OK)
//				.body(new ResponseObject("OK", "Successfully:" , customTask));
		}
	}
}
