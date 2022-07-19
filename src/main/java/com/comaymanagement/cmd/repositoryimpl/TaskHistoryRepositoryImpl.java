package com.comaymanagement.cmd.repositoryimpl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.TaskHis;
import com.comaymanagement.cmd.repository.ITaskHistory;

import net.bytebuddy.asm.Advice.This;

@Repository
@Transactional
public class TaskHistoryRepositoryImpl implements ITaskHistory {

	static final Logger LOGGER = LoggerFactory.getLogger(This.class); 
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public TaskHis add(TaskHis taskHis) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Integer id =(Integer) session.save(taskHis);
			if(id != -1) {
				taskHis.setId(id);
				return taskHis;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

}
