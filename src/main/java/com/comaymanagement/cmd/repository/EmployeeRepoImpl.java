package com.comaymanagement.cmd.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Employee;
@Repository
public class EmployeeRepoImpl  {
	@PersistenceContext
    private EntityManager entityManager;

    public List<Employee> findOrderedBySeatNumberLimitedTo(int limit) {
        return entityManager.createQuery("SELECT e.unique_number FROM cmd.employees e ORDER BY e.unique_number",
        		Employee.class)..setMaxResults(limit).getResultList();
    }
}
