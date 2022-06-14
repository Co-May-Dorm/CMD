package com.comaymanagement.cmd.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.model.User;


@Repository
public interface UserRepository{
    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
