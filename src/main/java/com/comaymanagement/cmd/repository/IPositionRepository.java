package com.comaymanagement.cmd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comaymanagement.cmd.entity.Position;

public interface IPositionRepository extends JpaRepository<Position, String>{

}
