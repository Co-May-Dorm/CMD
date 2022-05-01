package com.comaymanagement.cmd.repositoryimpl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.repository.ITeamRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class TeamRepositoryImpl implements ITeamRepository {

}
