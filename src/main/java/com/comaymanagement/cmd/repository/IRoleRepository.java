package com.comaymanagement.cmd.repository;

import java.util.List;

import com.comaymanagement.cmd.model.RoleModel;

public interface IRoleRepository {
	List<RoleModel> findAll(
			String name,
			String sort,
			String order,
			Integer limit,
			Integer offset);
	Integer CountTotalItem();
}
