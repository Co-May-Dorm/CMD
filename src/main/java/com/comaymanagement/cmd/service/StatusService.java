package com.comaymanagement.cmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.repositoryimpl.StatusRepositotyImpl;

@Service
public class StatusService {
	
	@Autowired
	StatusRepositotyImpl statusRepositoty;



	
}
