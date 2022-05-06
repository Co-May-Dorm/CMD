package com.comaymanagement.cmd.repository;

import java.util.List;

import com.comaymanagement.cmd.model.OptionModel;

public interface IOptionRepository {
	public List<OptionModel> findAll();
}
