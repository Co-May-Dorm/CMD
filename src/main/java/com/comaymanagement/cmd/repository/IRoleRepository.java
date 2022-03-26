package com.comaymanagement.cmd.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.comaymanagement.cmd.customentity.CustomRoleAll;

public interface IRoleRepository {
	List<CustomRoleAll> findAll(
			String name,
			String sort,
			String order,
			Integer limit,
			Integer offset);
	Integer CountTotalItem();
}
