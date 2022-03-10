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

@Service
public class TaskService implements IGeneralService<Task> {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaskRepositoryImpl taskRepository;

	public ResponseEntity<Object> findByStatusId(String statusId, String sort, String order, String page) {
		order = order == null ? "t.id" : order;
		sort = sort == null ? "DESC" : sort;		
		page = page == null ? "1" : page;
		try {
			int limit = 15;
			List<CustomTaskAll> taskListByStatusId = taskRepository.findByStatusId(statusId,sort,order,page,limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.CountTotalItem());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("taskList", taskListByStatusId);
			results.put("pagination", pagination);
			if(taskListByStatusId == null) {
				LOGGER.info("Have no task by status_id: " + statusId );
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("","Have no task by status_id: " + statusId,""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",results));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}

	}

	public ResponseEntity<Object> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, String page) {
		dep = dep == null ? " " : dep;
		title = title == null ? " " : title;
		status = status == null ? " " : status;
		creator = creator == null ? " " : creator;
		receiver = receiver == null ? " " : receiver;
		createDate = createDate == null ? " " : createDate;
		finishDate = finishDate == null ? " " : finishDate;
		order = order == null ? "t.id" : order;
		sort = sort == null ? "DESC" : sort;		
		page = page == null ? "1" : page;
		List<CustomTaskAll> taskList = new ArrayList<CustomTaskAll>();
		try {
			int limit = 15;
			taskList = taskRepository.findAllTask(dep, title, status, creator, receiver, createDate,
					finishDate, sort, order, page,limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(taskRepository.CountTotalItem());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("taskList", taskList);

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
