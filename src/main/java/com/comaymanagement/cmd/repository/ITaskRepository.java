package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.model.TaskModel;


public interface ITaskRepository {
	
	List<TaskModel> findByStatusId(
			@Param("status_id") String statusId,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") Integer offset,
			@Param("limit") Integer limit);
	
	List<TaskModel> findAll( 
			@Param("dep") String dep, 
			@Param("title") String title, 
			@Param("status") String status, 
			@Param("creator") String creator, 
			@Param("receiver") String receiver,
			@Param("createDate") String createDate,
			@Param("finishDate") String finishDate,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") Integer offset,
			@Param("limit") Integer limit);
	Integer countAllPaging(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order);
	Integer countFindByIds(List<Integer> ids);
	List<TaskModel> findByStatusIds(
			@Param("status_id") List<Integer> statusIds,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") Integer offset,
			@Param("limit") Integer limit);
	TaskModel add(Task task) throws Exception;
	Integer getMaxId();
	TaskModel findById(Integer id);
	String deleteTaskById(Integer id);
	TaskModel edit(Task task) throws Exception;
	List<TaskModel> filter(
			@Param("createFrom") String createFrom,
			@Param("createTo") String createTo,
			@Param("finishFrom") String finishFrom,
			@Param("finishTo") String finishTo,
			@Param("title") String title,
			@Param("creator") String creator,
			@Param("receicer") String receiver,
			@Param("department") String department,
			@Param("limit") Integer limit,
			@Param("order") String order,
			@Param("page") String page,
			@Param("sort") String sort
	);
	Integer countFilter(
			@Param("createFrom") String createFrom,
			@Param("createTo") String createTo,
			@Param("finishFrom") String finishFrom,
			@Param("finishTo") String finishTo,
			@Param("title") String title,
			@Param("creator") String creator,
			@Param("receicer") String receiver,
			@Param("department") String department
	);
}
