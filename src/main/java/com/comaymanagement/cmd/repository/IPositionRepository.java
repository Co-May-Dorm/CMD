package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.customentity.CustomPositionAll;

public interface IPositionRepository{

	List<CustomPositionAll> findAllByRoleId(
			@Param("roleID") Integer roleId);
}
