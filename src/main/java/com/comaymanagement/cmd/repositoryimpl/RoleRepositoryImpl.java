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
	public List<CustomRoleAll> findAllRole(String sort, String order, String page) {
		order = order == null ? "r.unique_number" : order.trim();
		int limit =10;
		sort = sort == null ? "DESC" : sort.trim();
		page = page == null ? "1" : page.trim();
		
		List<Role> roles = new ArrayList<Role>();
		List<CustomRoleAll> customRoleList = new ArrayList<CustomRoleAll>();
		StringBuilder hql = new StringBuilder("FROM roles AS r ");
		
		try {
			
			int offset = (Integer.valueOf(page) - 1) * limit;
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
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


}
