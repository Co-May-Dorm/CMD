package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Task;


public interface ITaskRepository {
	
	List<CustomTaskAll> findByStatusId(
			@Param("status_id") String statusId,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") String page,
			@Param("limit") Integer limit);
	
	List<CustomTaskAll> findAll( 
			@Param("dep") String dep, 
			@Param("title") String title, 
			@Param("status") String status, 
			@Param("creator") String creator, 
			@Param("receiver") String receiver,
			@Param("createDate") String createDate,
			@Param("finishDate") String finishDate,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") String page,
			@Param("limit") Integer limit);
	Integer countAll(String dep, String title, String status, String creator, String receiver);
	Integer countFindByIds();
	List<CustomTaskAll> findByStatusIds(
			@Param("status_id") List<String> statusIds,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("offset") String page,
			@Param("limit") Integer limit);
	Integer save(Task task);
	Integer getMaxId();
}
