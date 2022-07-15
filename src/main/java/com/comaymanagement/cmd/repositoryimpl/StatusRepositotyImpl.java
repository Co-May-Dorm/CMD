package com.comaymanagement.cmd.repositoryimpl;

import java.util.Iterator;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.comaymanagement.cmd.entity.Status;
import com.comaymanagement.cmd.repository.IStatusRepositoty;

import net.bytebuddy.asm.Advice.This;
@Repository
@Transactional
public class StatusRepositotyImpl implements IStatusRepositoty {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(This.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Status findById(Integer id) {
		Status status = new Status();
		StringBuilder hql = new StringBuilder("FROM statuses AS st ");
		hql.append("WHERE st.id = :id");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("id",id);			
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				status = (Status) it.next();
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return status;
	}

}
