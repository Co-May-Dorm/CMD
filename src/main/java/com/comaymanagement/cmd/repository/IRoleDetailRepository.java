package com.comaymanagement.cmd.repository;

import java.util.List;

import com.comaymanagement.cmd.entity.RoleDetail;
import com.comaymanagement.cmd.model.RoleDetailModel;

public interface IRoleDetailRepository {
	public Integer add(RoleDetail roleDetail);
	public Integer delete(Integer id);
	public List<RoleDetail> findAllByRoleId(Integer roleId);
}
