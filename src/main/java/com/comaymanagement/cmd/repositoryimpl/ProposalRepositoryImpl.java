package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.ApprovalStep;
import com.comaymanagement.cmd.entity.Proposal;
import com.comaymanagement.cmd.model.ProposalModel;
import com.comaymanagement.cmd.repository.IProposalRepository;

import net.bytebuddy.asm.Advice.This;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProposalRepositoryImpl implements IProposalRepository {

	@Autowired
	SessionFactory sessionFactory;

	private final Logger LOGGER = LoggerFactory.getLogger(This.class);

	@Override
	public List<ProposalModel> findAll(String proposalType, String content, String status, String creator,
			String createDate, String finishDate, String sort, String order, Integer offset, Integer limit) {
		List<ApprovalStep> appSteps = new ArrayList<>(); 
//		List<ProposalModel> proposalModels = new ArrayList<>();
//		StringBuilder hql = new StringBuilder("FROM proposals AS pro ");
////		hql.append("INNER JOIN pro.employee AS em ");
////		hql.append("INNER JOIN pro.proposalType AS pt ");
////		hql.append("INNER JOIN pro.status AS st ");
////		hql.append("INNER JOIN pro.proposalDetails AS pd ");
////		hql.append("WHERE pd.fieldId = 1 ");
////		hql.append("AND pt.name LIKE CONCAT('%',:proposal,'%') ");
////		hql.append("AND st.name LIKE CONCAT('%',:status,'%') ");
////		hql.append("AND em.name LIKE CONCAT('%',:creator,'%') ");
////		hql.append("AND pd.content LIKE CONCAT('%',:content,'%') ");
////		hql.append("AND pro.createDate LIKE CONCAT('%',:createDate,'%') ");
////		hql.append("ORDER BY " + sort + " " + order);
//		try {
//			Session session = sessionFactory.getCurrentSession();
//			LOGGER.info(hql.toString());
//			Query query = session.createQuery(hql.toString());
//			query.setParameter("proposal", proposal);
//			query.setParameter("status", status);
//			query.setParameter("creator", creator);
//			query.setParameter("content", content);
//			query.setParameter("createDate", createDate);
//			query.setFirstResult(offset);
//			query.setMaxResults(limit);
//			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
//				Object[] objects = (Object[]) it.next();
//				ProposalModel proposalModel = new ProposalModel();
//				Proposal proposalTemp = (Proposal) objects[0];
//				Employee employee = (Employee) objects[1];
//				EmployeeModel empModel = new EmployeeModel();
//				empModel.setId(employee.getId());
//				empModel.setName(employee.getName());
//				ProposalType proposalType = (ProposalType) objects[2];
//				Status statusTemp = (Status) objects[3];
//				ProposalDetail proposalDetail = (ProposalDetail) objects[4];
//				proposalModel.setId(proposalTemp.getId());
////				proposalModel.setContent(proposalDetail.getContent());
//				proposalModel.setCreatedDate(proposalTemp.getCreateDate());
//				proposalModel.setCreator(empModel);
//				proposalModel.setProposalType(proposalType);
//				proposalModel.setStatus(statusTemp);
//				proposalModels.add(proposalModel);
//
//			}
//
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//		}
		
//		appSteps = findApprovalStepDetail("45", "", "");
		List<Proposal> proposals = new ArrayList<>();
		for(ApprovalStep appStep : appSteps) {
			String proposalTypeId = appStep.getProposalType().getId().toString();
			String step = appStep.getApprovalStepIndex();
			List<Proposal> proposalsTMP = new ArrayList<>();
			proposalsTMP = findAllProposalPretreatment(proposalType, content, status, creator, createDate, finishDate, proposalTypeId, step, sort, order);
			// store proposal of each proposalType and step
			if(proposalsTMP!=null || proposalsTMP.size()>0) {
				for(Proposal pro : proposalsTMP) {
					proposals.add(pro);
				}
			}
		}
		
		
		
		return null;
	}
	
	public boolean checkExitsInStep(String employeeId) {
		// Check all record of approvalStepDetail is exist empId or empId exist in department or position
		return false;
	}
	public List<ApprovalStep> findApprovalStepDetail(Integer employeeId){
		StringBuilder hql = new StringBuilder();
		List<ApprovalStep> approvalSteps = new ArrayList<>();
		//Checl if employeeId cannot found => check employeeId in department and position
		hql.append("from approval_steps appStep ");
		hql.append("inner join appStep.approvalStepDetails as appStepDetail ");
		hql.append("where appStepDetail.employeeId = :employeeId ");
		try {
			Session session = sessionFactory.getCurrentSession();
			LOGGER.info(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setParameter("employeeId", employeeId);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] objects = (Object[]) it.next();
				ApprovalStep approvalStep = (ApprovalStep) objects[0]; 
				approvalSteps.add(approvalStep);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.toString());
			return null;
		}
		return approvalSteps;
	}
	
	public List<Proposal> findAllProposalPretreatment(String proposalType, String content, String status, String creator,
			String createDate, String finishDate,String proposalTypeId, String step, String sort, String order){
		List<Proposal> proposals = new ArrayList<>();
		StringBuilder hql = new StringBuilder("FROM proposals AS pro ");
		hql.append("INNER JOIN pro.employee AS em ");
		hql.append("INNER JOIN pro.proposalType AS pt ");
		hql.append("INNER JOIN pro.status AS st ");
		hql.append("INNER JOIN pro.proposalDetails AS pd ");
		hql.append("WHERE pd.fieldId = 1 ");
		hql.append("AND pt.name LIKE CONCAT('%',:proposalType,'%') ");
		hql.append("AND st.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND em.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND pd.content LIKE CONCAT('%',:content,'%') ");
		hql.append("AND pro.createDate LIKE CONCAT('%',:createDate,'%') ");
		hql.append("AND pt.id LIKE CONCAT('%',:proposalTypeId,'%') ");
		hql.append("AND pro.currentStep >= :step  ");
		hql.append("ORDER BY " + sort + " " + order);
		try {
			Session session = sessionFactory.getCurrentSession();
			LOGGER.info(hql.toString());
			Query query = session.createQuery(hql.toString());
			query.setParameter("proposalType", proposalType);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("content", content);
			query.setParameter("createDate", createDate);
			query.setParameter("proposalTypeId", proposalTypeId);
			query.setParameter("step", step);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] objects = (Object[]) it.next();
				Proposal proposalTemp = (Proposal) objects[0];
				proposals.add(proposalTemp);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		return proposals;
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
