package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.customentity.CustomRoleAll;

public interface IRoleRepository {
	List<CustomRoleAll> findAllRole(
			@Param("sort") String sort,
			@Param("order") String order,
			@Param("limit") Integer limit,
			@Param("page") String page);
}
