package com.comaymanagement.cmd.repositoryimpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repository.ITaskRepository;
@Repository
public class TaskRepositoryImpl implements ITaskRepository{

	@Override
	public List<Task> findByStatusId(String statusId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, Integer limit, Integer offset) {
		// TODO Auto-generated method stub
		return null;
	}

}
