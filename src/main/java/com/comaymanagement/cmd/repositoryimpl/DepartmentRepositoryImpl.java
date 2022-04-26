package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
public class DepartmentRepositoryImpl implements IDepartmentRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	@Autowired
	private SessionFactory sessionFactory;


	
	@Override
	@Transactional
	public Set<CustomDepartmentAll> findAll(String name) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from departments dep inner join dep.positions as pos where dep.name like CONCAT('%',:name,'%')";
		Set<CustomDepartmentAll> cusDepSet = new LinkedHashSet<CustomDepartmentAll>();
		
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				List<CustomPositionAll> cusPosList = new ArrayList<>();
				Object[] ob = (Object[]) it.next();
				CustomDepartmentAll cusDep = new CustomDepartmentAll();
				Department tmp = (Department) ob[0];
				cusDep.setId(tmp.getId());
				cusDep.setCode(tmp.getCode());
				cusDep.setName(tmp.getName());
				cusDep.setDescription(tmp.getDescription());
				cusDep.setFatherDepartmentId(tmp.getFatherDepartmentId());
				cusDep.setLevel(tmp.getLevel());
				for (Position pos : tmp.getPositions()) {
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
				cusDep.setPositions(cusPosList);
				cusDepSet.add(cusDep);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at findAll() ", e);
		}
		return cusDepSet;
	}

	@Transactional
	public boolean isExisted(Integer id, String code) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from departments dep where dep.code = :code and dep.id != :id";
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("code", code);
			query.setParameter("id", id);
			List list = query.getResultList();
			if (list.size()>0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at isExisted() ", e);
		}
		return false;
	}

	@Transactional
	@Override
	public Integer save(Department dep) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Integer id = (Integer) session.save(dep);
			return id;
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at add() ", e);
		}
		return -1;
	}

	@Override
	public Integer edit(Department dep) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(dep);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl at edit() ", e);
			return 0;
		}
	}

	@Override
	public Integer delete(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Employee emp = new Employee();
			emp = session.find(Employee.class, id);
			session.remove(emp);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured in delete() ", e);
			return 0;
		}

	}

	@Override
	public Department findById(Integer id) {

		Session session = sessionFactory.getCurrentSession();
		StringBuilder hql = new StringBuilder();
		Department department = null;
		hql.append("from departments dep where dep.id = " + id);
		try {
			Query query = session.createQuery(hql.toString());
			department = (Department) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Error has occured in delete() ", e);
		}
		
		return department;
	}

	@Override
	public Department findByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder hql = new StringBuilder();
		Object department = null;
		hql.append("from departments dep where dep.name = :name");
		
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			LOGGER.info(hql.toString());
			department =  query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Error has occured in DepartmentRepositoryImpl findByName() ", e);
		}
		return (Department)department;
	}

}
