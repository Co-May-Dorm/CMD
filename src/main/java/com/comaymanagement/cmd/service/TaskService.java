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
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
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
		order = order == null ? "t.id" : order;
		sort = sort == null ? "DESC" : sort;
		page = page == null ? "1" : page;
		try {
			int limit = 15;
			List<CustomTaskAll> tasksByStatusId = taskRepository.findByStatusId(statusId, sort, order, page, limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.CountTotalItemTaskAll());
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
		order = order == null ? "t.id" : order.trim();
		sort = sort == null ? "DESC" : sort.trim();
		page = page == null ? "1" : page.trim();
		List<CustomTaskAll> tasks = new ArrayList<CustomTaskAll>();
		try {
			int limit = 15;
			tasks = taskRepository.findAllTask(dep, title, status, creator, receiver, createDate, finishDate, sort,
					order, page, limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.CountTotalItemTaskAll());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("tasks", tasks);

			if (results.size() > 0) {
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

	@SuppressWarnings("unused")
	public ResponseEntity<Object> findByStatusIds(String json, String sort, String order, String page) {
		order = order == null ? "t.id" : order.trim();
		sort = sort == null ? "DESC" : sort.trim();
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
			pagination.setTotalItem(taskRepository.CountTotalItemFindByIds());
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

	public ResponseEntity<Object> save(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(CustomTaskAll model) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseEntity<Object> save(String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
