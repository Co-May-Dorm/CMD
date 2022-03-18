package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.repository.IPositionRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class PositionRepositoryImpl implements IPositionRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(PositionRepositoryImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<CustomPositionAll> findAllByRoleId(Integer roleId) {
		StringBuilder hql = new StringBuilder("FROM positions WHERE role_id = :roleId");
		List <CustomPositionAll> customPositionAlls = new ArrayList<CustomPositionAll>();
		List <Position> positions = new ArrayList<Position>();

		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("roleId", roleId);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object obj = (Object) it.next();
				Position po = (Position) obj;
				positions.add(po);
			}
			for(Position po : positions) {
				CustomPositionAll customPositionAll = new CustomPositionAll();
				customPositionAll.setId(po.getId());
				customPositionAll.setName(po.getName());
				customPositionAll.setIsManager(po.getIsManager());
				customPositionAll.setRole(po.getRole());
				customPositionAlls.add(customPositionAll);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return customPositionAlls;
	}

	@Override
	public Integer CountTotalItem() {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM positions");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			count = Integer.valueOf(list.get(0).toString());
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Integer save(Position p) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Integer id = (Integer) session.save(p);
			return id;
		} catch (Exception e) {
			LOGGER.error("Error has occured in PositionRepositoryImpl at save() ", e);
			return -1;
		}
	}
	@Override
	public Integer edit(Position p) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(p);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at edit() ", e);
			return 0;
		}
	}
	
}
