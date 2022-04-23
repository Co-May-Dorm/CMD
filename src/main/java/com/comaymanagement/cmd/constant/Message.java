package com.comaymanagement.cmd.constant;

import org.springframework.beans.factory.annotation.Autowired;

import com.comaymanagement.cmd.repositoryimpl.MessageRepositoryImpl;

public class Message {
	@Autowired
	static MessageRepositoryImpl messageRepositoryImpl;
	public static String getMessage(Integer id) {
		return messageRepositoryImpl.getMessage(id);
	}
	
}
