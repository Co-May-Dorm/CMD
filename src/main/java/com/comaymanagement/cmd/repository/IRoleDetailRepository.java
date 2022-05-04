package com.comaymanagement.cmd.repository;

import com.comaymanagement.cmd.model.RoleDetailModel;

public interface IRoleDetailRepository {
	public RoleDetailModel findAllByRoleId(Integer roleId);
}
