package com.comaymanagement.cmd.repository;

import java.util.List;
import java.util.Set;

import com.comaymanagement.cmd.customentity.CustomEmployeeAll;
import com.comaymanagement.cmd.entity.Employee;

public interface IEmployeeRepository {

	public List<Employee> findByActiveFlag(Boolean activeFlag);

//	@Query(value = "select * from cmd.employees emp "
//			+ "inner join cmd.positions_employees pos_emp on emp.id = pos_emp.employee_id "
//			+ "inner join cmd.positions pos on pos.id = pos_emp.position_id "
//			+ "inner join cmd.departments dep on emp.department_id = dep.id " 
//			+ " where  emp.name like CONCAT('%',:name,'%') "
//			+ "and emp.date_of_birth like CONCAT('%',:dob,'%') "
//			+ "and emp.email like CONCAT('%',:email,'%') "
//			+ "and emp.phone_number like CONCAT('%',:phone,'%') "
//			+ "and dep.name like CONCAT('%',:dep,'%') "
//			+ "and pos.name like CONCAT('%',:pos,'%') "
//			+ "order by :sort :order "
//			+ "limit :limit offset :offset", nativeQuery = true)
//	public List<Employee> employeePaging(
//										@Param("name") String name, 
//										@Param("dob") String dob, 
//										@Param("email") String email, 
//										@Param("phone") String phone, 
//										@Param("dep") String dep, 
//										@Param("pos") String pos,
//										@Param("sort") String sort,
//										@Param("order") String order,
//										@Param("limit") Integer limit,
//										@Param("offset") Integer offset
//										);
	public Set<CustomEmployeeAll> findAll(String name, String dob, String email, String phone, String dep, String pos,
			String sort, String order, Integer limit, Integer offset);

	public Integer add(Employee emp);

	public Integer edit(Employee emp);

	public String delete(Employee emp);

	public boolean checkEmployeeCodeExisted(Integer id, String code);
	public Integer countAllPaging(String name, String dob, String email, String phone, String dep,
			String pos, String sort, String order);
	public Employee findById(Integer id);
	
}
