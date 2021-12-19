package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.repository.IPositionRepository;
@Service
public class PositionService implements IGeneralService<Position>{
	@Autowired
	IPositionRepository positionRepository;

	
	@Override
	public List<Position> findAll() {
		return positionRepository.findAll();
	}

	@Override
	public Optional<Position> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position save(Position t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Position model) {
		// TODO Auto-generated method stub
		
	}

}
