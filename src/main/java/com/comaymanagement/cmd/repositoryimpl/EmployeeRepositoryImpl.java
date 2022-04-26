package com.comaymanagement.cmd.repositoryimpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.customentity.CustomTeamAll;
import com.comaymanagement.cmd.customentity.User;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.entity.Team;
import com.comaymanagement.cmd.repository.IEmployeeRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public class EmployeeRepositoryImpl implements IEmployeeRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
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
	public Set<CustomEmployeeAll> findAll(String name, String dob, String email, String phone, String dep,
			String pos, String sort, String order, Integer limit, Integer offset) {
		Set<Employee> employeeSet = new LinkedHashSet<>();
		StringBuilder hql = new StringBuilder();
		hql.append("from employees emp ");
		hql.append("inner join emp.positions as pos inner join emp.departments as dep ");
		hql.append("where emp.name like CONCAT('%',:name,'%') ");
		hql.append("and emp.dateOfBirth like CONCAT('%',:dob,'%') ");
		hql.append("and emp.email like CONCAT('%',:email,'%') ");
		hql.append("and emp.phoneNumber like CONCAT('%',:phone,'%') ");
		hql.append("and dep.name like CONCAT('%',:dep,'%') ");
		hql.append("and pos.name like CONCAT('%',:pos,'%') ");
		hql.append("and pos.team.id is null ");
		hql.append("and pos.department.id is not null ");
		hql.append("and emp.activeFlag = true ");
		hql.append("order by " + sort + " " + order);
		Session session = this.sessionFactory.getCurrentSession();
		Set<Employee> empSet = new LinkedHashSet<>();
		Set<CustomEmployeeAll> cusEmpSet = new LinkedHashSet<>();
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
				Employee e = (Employee)ob[0];
				empSet.add(e);
			}
			for(Employee e : empSet) {
				CustomEmployeeAll cusEmp = new CustomEmployeeAll();
				List<CustomPositionAll> cusPositionList = new ArrayList<>();
				List<CustomDepartmentAll> cusDepartmentList = new ArrayList<>();
				List<CustomTeamAll> cusTeamList= new ArrayList<>();
				// Add department list
				List<Department> departList = e.getDepartments();
				for(Department d : departList) {
					CustomDepartmentAll cusDep = new CustomDepartmentAll();
					cusDep.setId(d.getId());
					cusDep.setName(d.getName());
					cusDep.setHeadPosition(d.getHeadPosition());
					cusDep.setLevel(d.getLevel());
					cusDepartmentList.add(cusDep);
				}
				// Add team list
				List<Team> teams = e.getTeams();
				for(Team t : teams) {
					CustomTeamAll cusTeam = new CustomTeamAll();
					cusTeam.setId(t.getId());
					cusTeam.setName(t.getName());
					cusTeam.setDescription(t.getDescription());
					cusTeam.setHeadPosition(t.getHeadPosition());
//					cusTeam.setPositions(t.getPositions());
					cusTeamList.add(cusTeam);
				}
				// Add position list
				for (Position p : e.getPositions()) {
					if(p.getDepartment()!=null && p.getTeam()==null) {
						CustomPositionAll cusPos = new CustomPositionAll();
						Role role = new Role();
						role.setId(p.getRole().getId());
						role.setName(p.getRole().getName());
						cusPos.setId(p.getId());
						cusPos.setName(p.getName());
						cusPos.setIsManager(p.getIsManager());
						cusPos.setRole(role);
						cusPositionList.add(cusPos);
					}
					
				}
				User user = new User();
				user.setUsername(e.getUsername());
				user.setEnableLogin(e.isEnableLogin());
				cusEmp.setId(e.getId());
				cusEmp.setCode(e.getCode());
				cusEmp.setName(e.getName());
				cusEmp.setAvatar(e.getAvatar());
				cusEmp.setGender(e.getGender());
				cusEmp.setDateOfBirth(e.getDateOfBirth());
				cusEmp.setEmail(e.getEmail());
				cusEmp.setPhoneNumber(e.getPhoneNumber());
				cusEmp.setActive(e.isActive());
				cusEmp.setCreateDate(e.getCreateDate());
				cusEmp.setDepartments(cusDepartmentList);;
				cusEmp.setPositions(cusPositionList);
				cusEmp.setUser(user);
				cusEmp.setCreateDate(e.getCreateDate());
				cusEmp.setModifyDate(e.getModifyDate());
				cusEmp.setCreateBy(e.getCreateBy());
				cusEmp.setTeams(cusTeamList);
				cusEmpSet.add(cusEmp);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in employeePaging() ", e);

		}

		return cusEmpSet;
	}
	
	@Transactional
	public boolean checkEmployeeCodeExisted(Integer id, String code) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "select count(*) from employees emp where emp.code = :code and emp.id != :id";
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("code", code);
			query.setParameter("id", id);
			List list = query.getResultList();
			Integer count = Integer.valueOf(list.get(0).toString());
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in checkEmployeeIdExisted() ", e);
		}

		return false;
	}

	@Override
	@Transactional
	public Integer add(Employee emp) {
		Session session = sessionFactory.getCurrentSession();
		try {

			return Integer.parseInt(session.save(emp).toString());
		} catch (Exception e) {
			LOGGER.error("Error has occured in addEmployee() ", e);
			return -1;
		}

	}

	@Override
	@Transactional
	public Integer edit(Employee emp) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(emp);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured in editEmployee() ", e);
			return 0;
		}
	}

	@Transactional
	public String delete(Employee emp) {
		try {
			return String.valueOf(edit(emp));
		} catch (Exception e) {
			LOGGER.error("Error has occured in delete() ", e);
			return "0";
		}

	}
	 	
	@Transactional
	@Override
	public Integer countAllPaging(String name, String dob, String email, String phone, String dep,
			String pos, String sort, String order) {
		Set<Employee> employeeSet = new LinkedHashSet<>();
		StringBuilder hql = new StringBuilder();
		hql.append("from employees emp ");
		hql.append("inner join emp.positions as pos inner join emp.departments as dep ");
		hql.append("where emp.name like CONCAT('%',:name,'%') ");
		hql.append("and emp.dateOfBirth like CONCAT('%',:dob,'%') ");
		hql.append("and emp.email like CONCAT('%',:email,'%') ");
		hql.append("and emp.phoneNumber like CONCAT('%',:phone,'%') ");
		hql.append("and dep.name like CONCAT('%',:dep,'%') ");
		hql.append("and pos.name like CONCAT('%',:pos,'%') ");
		hql.append("and pos.team.id is null ");
		hql.append("and pos.department.id is not null ");
		hql.append("and emp.activeFlag = true ");
		hql.append("order by " + sort + " " + order);
		Session session = this.sessionFactory.getCurrentSession();
		List<CustomEmployeeAll> cusEmpList = new ArrayList();
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("name", name);
			query.setParameter("dob", dob);
			query.setParameter("email", email);
			query.setParameter("phone", phone);
			query.setParameter("dep", dep);
			query.setParameter("pos", pos);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] ob = (Object[]) it.next();
				employeeSet.add((Employee) ob[0]);
			}
			
		} catch (Exception e) {
			LOGGER.error("Error has occured in employeePaging() ", e);
			
		}
		
		return employeeSet.size();
	}
	@Override
	public Employee findById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Employee employee = null;
		try {
			employee = session.find(Employee.class, id);
		} catch (Exception e) {
			LOGGER.error("Error has occured in findById() ", e);
		}
		return employee;
	}

	@Override
	public boolean add(Set<Employee> emps) {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			int count = 0;
			for (Employee em : emps) {
				Integer id = -1;
				id = add(em);
				count++;
			}
			if(count == emps.size()) {
				connection.commit();
				return true;
			}else {
				connection.rollback();
				return false;
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage());
			}
			return false;
		}

	}
}
