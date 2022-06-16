package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Proposal;
import com.comaymanagement.cmd.entity.ProposalDetail;
import com.comaymanagement.cmd.entity.ProposalType;
import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.model.ProposalModel;
import com.comaymanagement.cmd.repository.IProposalRepository;

import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.utility.privilege.GetSystemPropertyAction;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProposalRepositoryImpl implements IProposalRepository {

	@Autowired
	SessionFactory sessionFactory;

	private final Logger LOGGER = LoggerFactory.getLogger(This.class);

	@Override
	public List<ProposalModel> findAll(String proposal, String content, String status, String creator,
			String createDate, String finishDate, String sort, String order, Integer offset, Integer limit) {

		List<ProposalModel> proposalModels = new ArrayList<>();
		StringBuilder hql = new StringBuilder("FROM proposals AS pro ");
		hql.append("INNER JOIN pro.employee AS em ");
		hql.append("INNER JOIN pro.proposalType AS pt ");
		hql.append("INNER JOIN pro.status AS st ");
		hql.append("INNER JOIN pro.proposalDetails AS pd ");
		hql.append("WHERE pd.proposalDetailIndex = 1 ");
		hql.append("AND pt.name LIKE CONCAT('%',:proposal,'%') ");
		hql.append("AND st.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND em.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND pd.content LIKE CONCAT('%',:content,'%') ");
		hql.append("AND pro.createDate LIKE CONCAT('%',:createDate,'%') ");
		hql.append("ORDER BY " + sort + " " + order);
		try {
			Session session = sessionFactory.getCurrentSession();
			LOGGER.info(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setParameter("proposal", proposal);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("content", content);
			query.setParameter("createDate", createDate);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] objects = (Object[]) it.next();
				ProposalModel proposalModel = new ProposalModel();
				Proposal proposalTemp = (Proposal) objects[0];
				Employee employee = (Employee) objects[1];
				ProposalType proposalType = (ProposalType) objects[2];
				Status statusTemp = (Status) objects[3];
				ProposalDetail proposalDetail = (ProposalDetail) objects[4];
				proposalModel.setId(proposalTemp.getId());
				proposalModel.setContent(proposalDetail.getContent());
				proposalModel.setCreatedDate(proposalTemp.getCreateDate());
				proposalModel.setCreator(employee.getName());
				proposalModel.setProposalType(proposalType);
				proposalModel.setStatus(statusTemp);
				proposalModels.add(proposalModel);

			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return proposalModels;
	}
	
	@Override
	public Integer countAllPaging(String proposal, String content, String status, String creator,
			String createDate, String finishDate, String sort, String order, Integer offset, Integer limit) {
		List<ProposalModel> proposalModels = new ArrayList<>();
		StringBuilder hql = new StringBuilder("FROM proposals AS pro ");
		hql.append("INNER JOIN pro.employee AS em ");
		hql.append("INNER JOIN pro.proposalType AS pt ");
		hql.append("INNER JOIN pro.status AS st ");
		hql.append("INNER JOIN pro.proposalDetails AS pd ");
		hql.append("WHERE pd.proposalDetailIndex = 1 ");
		hql.append("AND pt.name LIKE CONCAT('%',:proposal,'%') ");
		hql.append("AND st.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND em.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND pd.content LIKE CONCAT('%',:content,'%') ");
		hql.append("AND pro.createDate LIKE CONCAT('%',:createDate,'%') ");
		hql.append("ORDER BY " + sort + " " + order);
		int count = -1;
		try {
			Session session = sessionFactory.getCurrentSession();
			LOGGER.info(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setParameter("proposal", proposal);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("content", content);
			query.setParameter("createDate", createDate);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] objects = (Object[]) it.next();
				ProposalModel proposalModel = new ProposalModel();
				proposalModels.add(proposalModel);

			}
			count = proposalModels !=null ? proposalModels.size() : 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

}
