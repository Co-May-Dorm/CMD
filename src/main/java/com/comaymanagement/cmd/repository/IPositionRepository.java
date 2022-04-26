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
	Integer add(Position p);
	Integer edit(Position p);
	Position findById(Integer id);
	List<Position> findAllByDepId(Integer depId);
}
