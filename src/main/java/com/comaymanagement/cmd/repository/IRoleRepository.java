package com.comaymanagement.cmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comaymanagement.cmd.entity.Role;

public interface IRoleRepository extends JpaRepository<Role, String>{

}
