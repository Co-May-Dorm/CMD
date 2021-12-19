package com.comaymanagement.cmd.service;

import java.util.Optional;

public interface IGeneralService<T> {
	
    Iterable<T> findAll();

    Optional<T> findById(String id);

    T save(T t);

    void remove(T model);
    
}
