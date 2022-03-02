package com.comaymanagement.cmd.repositoryimpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.repository.IDepartmentRepository;
@Repository
public class DepartmentRepositoryImpl implements IDepartmentRepository{

	@Override
	public List<Department> findAllDepartmentByEmployeeId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
