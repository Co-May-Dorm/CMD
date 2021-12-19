package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comaymanagement.cmd.entity.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, String> {
	
	public List<Employee> findByActiveFlag(Boolean activeFlag);
	

	
}
