package com.comaymanagement.cmd.repositoryimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.repository.ITeamRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class TeamRepositoryImpl implements ITeamRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer edit(Team team) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(team);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at edit() ", e);
			return 0;
		}
	}
}
