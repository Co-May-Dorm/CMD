package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.customentity.User;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;

@Service
public class EmployeeService implements IGeneralService<Employee> {

	@Autowired
	EmployeeRepositoryImpl employeeRepository;
	
	public Set<CustomEmployeeAll> employeePaging(String name, String dob, String email, String phone, String dep, String pos, String sort, String order, Integer limit, Integer offset) {
		Set<Employee> employeeList = employeeRepository.employeePaging(name, dob, email, phone, dep, pos, sort, order, limit, offset);
		Set<CustomEmployeeAll> cusEmpList = new HashSet<>();
		for(Employee e : employeeList) {
			CustomEmployeeAll cusEmp = new CustomEmployeeAll();
			CustomDepartmentAll cusDep = new CustomDepartmentAll();
			List<CustomPositionAll> cusPositionList = new ArrayList<>();
			
			cusDep.setId(e.getDepartment().getId());
			cusDep.setName(e.getDepartment().getName());
			cusDep.setManagerId(e.getDepartment().getManagerId());
			// Add position list
			for(Position p : e.getPositionList()) {
				CustomPositionAll cusPos = new CustomPositionAll();
				cusPos.setId(p.getId());
				cusPos.setName(p.getName());
				cusPos.setIsManager(p.getIsManager());
				cusPos.setRoleId(p.getRoleId());
				cusPositionList.add(cusPos);
			}
			User user = new User();
			user.setUsername(e.getUsername());
			user.setEnableLogin(e.isEnableLogin());
			cusEmp.setUniqueNumber(e.getUniqueNumber());
			cusEmp.setId(e.getId());
			cusEmp.setName(e.getName());
			cusEmp.setAvatar(e.getAvatar());
			cusEmp.setGender(e.getGender());
			cusEmp.setDateOfBirth(e.getDateOfBirth());
			cusEmp.setEmail(e.getEmail());
			cusEmp.setPhoneNumber(e.getPhoneNumber());
			cusEmp.setDepartment(cusDep);
			cusEmp.setPositionList(cusPositionList);
			cusEmp.setUser(user);
			cusEmpList.add(cusEmp);
		}
		return cusEmpList;
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
