package com.comaymanagement.cmd.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;

public interface IDepartmentRepository{

	@Query(value = "Select * FROM cmd.departments dp inner join cmd.departments_employees de "
			+ "on dp.id = de.department_id " + "where de.employee_id = :id ", nativeQuery = true)
	public Set<CustomDepartmentAll> findAll(String name);
	public Integer save(Department dep);
	public Integer edit(Department dep);
	public Integer delete(Integer id);
	public boolean isExisted(Integer id, String code);
	public Department findById(Integer id);
	public Department findByName(String name);
}
