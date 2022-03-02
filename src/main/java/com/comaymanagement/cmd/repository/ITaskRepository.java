package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Task;

@Repository
public interface ITaskRepository {
	
	@Query( value = "Select * FROM tasks WHERE tasks.status_id = :status_id", nativeQuery = true)
	List<Task> findByStatusId(@Param("status_id") String statusId);
	
	@Query(value = "Select * FROM tasks t "
			+ "inner join employees e on t.creator_id = e.id "
			+ "inner join departments d on e.department_id = d.id "
			+ "inner join statuses s on t.status_id = s.id "
			+ "inner join (Select e1.unique_number,e1.id,e1.name  FROM tasks t "
			+ "inner join employees e1 on t.receiver_id = e1.id) r on t.receiver_id = r.id "
			+ "Where d.name LIKE CONCAT('%',:dep,'%') "
			+ "AND t.title LIKE CONCAT('%',:title,'%') "
			+ "AND s.name LIKE CONCAT('%',:status,'%') "
			+ "AND e.name LIKE CONCAT('%',:creator,'%') "
			+ "AND r.name LIKE CONCAT('%',:receiver,'%') "
			+ "AND t.create_date LIKE CONCAT('%',:createDate,'%')"
			+ "AND t.finish_date LIKE CONCAT('%',:finishDate,'%')"
			+ "order by :sort :order "
			+ "limit :limit offset :offset", nativeQuery = true)
	List<Task> findAllTask( 
			@Param("dep") String dep, 
			@Param("title") String title, 
			@Param("status") String status, 
			@Param("creator") String creator, 
			@Param("receiver") String receiver,
			@Param("createDate") String createDate,
			@Param("finishDate") String finishDate,
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("limit") Integer limit,
			@Param("offset") Integer offset);
}
