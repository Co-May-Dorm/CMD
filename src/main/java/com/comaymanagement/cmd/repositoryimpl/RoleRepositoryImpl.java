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
import com.comaymanagement.cmd.customentity.CustomRoleAll;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repository.IRoleRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class RoleRepositoryImpl implements IRoleRepository {

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	PositionRepositoryImpl positionRepositoryImpl;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);
	
	@Override
	public List<CustomRoleAll> findAll(String name, String sort, String order, Integer limit, Integer offset) {
		List<Role> roles = new ArrayList<Role>();
		List<CustomRoleAll> customRoleList = new ArrayList<CustomRoleAll>();
		StringBuilder hql = new StringBuilder();
		hql.append("FROM roles r ");
		hql.append("WHERE r.name like CONCAT('%',:name,'%') ");
		hql.append("ORDER BY " + sort + " " + order);
		
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			LOGGER.info(hql.toString());
			
			for(Iterator it = query.getResultList().iterator();it.hasNext();) {
				Object obj  = (Object) it.next();
				Role role = (Role) obj;
				roles.add(role);
			}
			for(Role role : roles) {
				CustomRoleAll customRoleAll = new CustomRoleAll();
				customRoleAll.setId(role.getId());
				customRoleAll.setName(role.getName());
				List<CustomPositionAll> customPositionAlls = positionRepositoryImpl.findAllByRoleId(role.getId());
				customRoleAll.setPositions(customPositionAlls);	
				customRoleList.add(customRoleAll);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return customRoleList;
	}

	@Override
	public Integer CountTotalItem() {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM roles");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			List list = query.getResultList();
			count = Integer.valueOf(list.get(0).toString());
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

}
