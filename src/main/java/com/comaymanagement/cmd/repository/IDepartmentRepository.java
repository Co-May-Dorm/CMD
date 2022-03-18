package com.comaymanagement.cmd.repository;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.comaymanagement.cmd.customentity.CustomDepartmentAll;
import com.comaymanagement.cmd.entity.Department;

public interface IDepartmentRepository{

	@Query(value = "Select * FROM cmd.departments dp inner join cmd.departments_employees de "
			+ "on dp.id = de.department_id " + "where de.employee_id = :id ", nativeQuery = true)
	public List<Department> findAllDepartmentByEmployeeId(@PathParam("id") String id);
	public List<CustomDepartmentAll> findAll(String name);
	public Integer save(Department dep);
	public String edit(Department dep);
	public String delete(String id);
	public boolean isExisted(Integer id, String code);
}
