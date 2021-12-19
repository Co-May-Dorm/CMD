package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.repository.IEmployeeRepository;

@Service
public class EmployeeService implements IGeneralService<Employee> {
	
	@Autowired
	IEmployeeRepository employeeRepository;

	@Override
	public Optional<Employee> findById(String id) {
		return employeeRepository.findById(id);
	}

	@Override
	public void remove(Employee employees) {
		employeeRepository.delete(employees);
	}
	@Override
	public Employee save(Employee employees) {
		return employeeRepository.save(employees);
	}

	@Override
	public List<Employee> findAll() {
		
		return employeeRepository.findAll();
	}
	public List<Employee> findByActiveFlag(Boolean flag){
		return employeeRepository.findByActiveFlag(flag);
	}
}
