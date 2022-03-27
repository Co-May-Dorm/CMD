package com.comaymanagement.cmd.repository;

import com.comaymanagement.cmd.customentity.CustomRoleDetailAll;

public interface IRoleDetailRepository {
	public CustomRoleDetailAll findAllByRoleId(Integer roleId);
}
