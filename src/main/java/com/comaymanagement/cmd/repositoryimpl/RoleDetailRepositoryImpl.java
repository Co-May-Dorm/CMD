package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.customentity.CustomOptionAll;
import com.comaymanagement.cmd.customentity.CustomPermissionAll;
import com.comaymanagement.cmd.customentity.CustomRoleAll;
import com.comaymanagement.cmd.customentity.CustomRoleDetailAll;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.entity.RoleDetail;
import com.comaymanagement.cmd.repository.IRoleDetailRepository;
import com.comaymanagement.cmd.repository.IRoleRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class RoleDetailRepositoryImpl implements IRoleDetailRepository {
	@Autowired
	SessionFactory sessionFactory;
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleRepositoryImpl.class);

	@Override
	@Transactional
	public CustomRoleDetailAll findAllByRoleId(Integer roleId) {
		List<RoleDetail> roleDetails = new ArrayList<>();
		StringBuilder hql = new StringBuilder();
		hql.append("FROM role_details rd ");
		hql.append("WHERE rd.role.id = :roleId ");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("roleId", roleId);
			List<CustomOptionAll> customOptions = new ArrayList<>();
			Set<Integer> tmpOptionId = new TreeSet<>();
			for(Iterator it = query.getResultList().iterator();it.hasNext();) {
				Object obj  = (Object) it.next();
				RoleDetail roleDetail = (RoleDetail) obj;
				roleDetails.add(roleDetail);
				tmpOptionId.add(roleDetail.getOptionId());
				
			}
			for(Integer i : tmpOptionId) {
				List<CustomPermissionAll> customPermissions = new ArrayList<>();
				CustomOptionAll customOption = new CustomOptionAll();
				for(RoleDetail rd : roleDetails) {
					if(rd.getOptionId().equals(i)) {
						CustomPermissionAll customPermission = new CustomPermissionAll();
						customPermission.setId(rd.getPermissionId());
						customPermissions.add(customPermission);
					}
					
				}
				customOption.setId(i);
				customOption.setCustomPermissions(customPermissions);
				customOptions.add(customOption);
			}
			System.out.println(tmpOptionId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	

}
