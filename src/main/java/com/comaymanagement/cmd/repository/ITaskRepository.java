package com.comaymanagement.cmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Task;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {

}
