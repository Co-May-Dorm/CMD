package com.comaymanagement.cmd.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

public interface IGeneralService<T> {

	Iterable<T> findAll();

	Optional<T> findById(String id);

	ResponseEntity<Object> save(T t);

	void remove(T model);

	ResponseEntity<Object> save(String json);

}
