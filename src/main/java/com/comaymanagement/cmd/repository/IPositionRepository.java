package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.customentity.CustomPositionAll;
import com.comaymanagement.cmd.entity.Position;

public interface IPositionRepository{
	List<CustomPositionAll> findAllByDepartmentId(Integer depId);
	List<CustomPositionAll> findAllByRoleId(
			@Param("roleID") Integer roleId);
	Integer CountTotalItem();
	Integer save(Position p);
	Integer edit(Position p);
}
