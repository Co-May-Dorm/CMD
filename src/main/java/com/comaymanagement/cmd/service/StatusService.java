package com.comaymanagement.cmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.repositoryimpl.StatusRepositotyImpl;

@Service
@Transactional(rollbackFor = Exception.class)
public class StatusService {
	
	@Autowired
	StatusRepositotyImpl statusRepositoty;



	
}
