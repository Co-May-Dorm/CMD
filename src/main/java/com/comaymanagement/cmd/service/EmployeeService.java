package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.repository.IEmployeeRepository;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;

@Service
public class EmployeeService implements IGeneralService<Employee> {

	@Autowired
	EmployeeRepositoryImpl employeeRepository;
	
	public List<Employee> employeePaging(String name, String dob, String email, String phone, String dep, String pos, String sort, String order, Integer limit, Integer offset) {
		return employeeRepository.employeePaging(name, dob, email, phone, dep, pos, sort, order, limit, offset);
	}

	@Override
	public Iterable<Employee> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Employee> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Employee save(Employee t) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void remove(Employee model) {
		// TODO Auto-generated method stub
		
	}

}
