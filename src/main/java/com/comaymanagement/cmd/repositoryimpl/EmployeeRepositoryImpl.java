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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.repository.IEmployeeRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public class EmployeeRepositoryImpl implements IEmployeeRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Employee> findByActiveFlag(Boolean activeFlag) {
		// TODO Auto-generated method stub
		return null;
	}

	// find all employee with position in department
	@Override
	@Transactional
	public List<Employee> employeePaging(String name, String dob, String email, String phone, String dep, String pos,
			String sort, String order, Integer limit, Integer offset) {
		List<Employee> employeeList = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append("from employees emp ");
		hql.append("inner join emp.positionList as pos inner join emp.department as dep ");
		hql.append("where emp.name like CONCAT('%',:name,'%') ");
		hql.append("and emp.dateOfBirth like CONCAT('%',:dob,'%') ");
		hql.append("and emp.email like CONCAT('%',:email,'%') ");
		hql.append("and emp.phoneNumber like CONCAT('%',:phone,'%') ");
		hql.append("and dep.name like CONCAT('%',:dep,'%') ");
		hql.append("and pos.name like CONCAT('%',:pos,'%') and pos.team is null and pos.department is not null ");
		hql.append("order by emp." + sort + " " + order);
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			query.setParameter("dob", dob);
			query.setParameter("email", email);
			query.setParameter("phone", phone);
			query.setParameter("dep", dep);
			query.setParameter("pos", pos);
			query.setFirstResult(offset);
			query.setMaxResults(limit);

			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] ob = (Object[]) it.next();
				employeeList.add((Employee) ob[0]);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in employeePaging() ", e);

		}

		return employeeList;
	}

	@Transactional
	public boolean checkEmployeeIdExisted(Integer uniqueNumber, String id) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from employees emp where emp.id = :id and emp.uniqueNumber != :uniqueNumber";
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			query.setParameter("uniqueNumber", uniqueNumber);
			List<Employee> list = (List<Employee>) query.getResultList();
			if (list.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in checkEmployeeIdExisted() ", e);
		}

		return false;
	}

	@Override
	@Transactional
	public String add(Employee emp) {
		Session session = sessionFactory.getCurrentSession();
		try {

			String id = (String) session.save(emp);
			return id;
		} catch (Exception e) {
			LOGGER.error("Error has occured in addEmployee() ", e);
			return "";
		}

	}

	@Override
	@Transactional
	public String edit(Employee emp) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(emp);
			return "1";
		} catch (Exception e) {
			LOGGER.error("Error has occured in editEmployee() ", e);
			return "0";
		}
	}

	@Transactional
	public String delete(String id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Employee emp = new Employee();
			emp = session.find(Employee.class, id);
			session.remove(emp);
			return "1";
		} catch (Exception e) {
			LOGGER.error("Error has occured in delete() ", e);
			return "0";
		}

	}

}
