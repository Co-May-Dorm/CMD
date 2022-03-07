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

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repository.IDepartmentRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class DepartmentRepositoryImpl implements IDepartmentRepository{
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Department> findAllDepartmentByEmployeeId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<CustomDepartmentAll> findAll(String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from departments dep inner join dep.positionList as pos where dep.name like CONCAT('%',:name,'%')";
		List<CustomDepartmentAll> cusDepList = new ArrayList<CustomDepartmentAll>();
		
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				List<CustomPositionAll> cusPosList = new ArrayList<>();
				Object[] ob = (Object[]) it.next();
				CustomDepartmentAll cusDep = new CustomDepartmentAll();
				Department tmp = (Department) ob[0];
				cusDep.setId(tmp.getId());
				cusDep.setName(tmp.getName());
				cusDep.setFatherDepartmentId(tmp.getFatherDepartmentId());
				for(Position pos : tmp.getPositionList()) {
					CustomPositionAll cusPos = new CustomPositionAll();
					Role role = new Role();
					role.setId(pos.getRole().getId());
					role.setName(pos.getRole().getName());
					cusPos.setId(pos.getId());
					cusPos.setName(pos.getName());
					cusPos.setIsManager(pos.getIsManager());
					cusPos.setRole(role);
					cusPosList.add(cusPos);
				}
				cusDep.setPositionList(cusPosList);
				if(cusDepList.size()<=0) {
					cusDepList.add(cusDep);
				}else {
					boolean duplicateValue = false;
					// check duplicate
					for(CustomDepartmentAll c : cusDepList) {
						if(cusDep.getId().equals(c.getId())) {
							duplicateValue = true;
							break;
						}
						
					}
					if(duplicateValue == false) {
						cusDepList.add(cusDep);
					}
				}
				
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at findAll() ", e);
		}
		return cusDepList;
	}
	
}
