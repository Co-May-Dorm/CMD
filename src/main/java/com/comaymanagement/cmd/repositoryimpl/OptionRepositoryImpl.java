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

import com.comaymanagement.cmd.entity.Option;
import com.comaymanagement.cmd.model.OptionModel;
import com.comaymanagement.cmd.repository.IOptionRepository;
@Repository
public class OptionRepositoryImpl implements IOptionRepository{
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<OptionModel> findAll() {
		Session session = sessionFactory.getCurrentSession();
		List<OptionModel> optionModelList = new ArrayList<>();
		StringBuilder hql = new StringBuilder(); 
		hql.append("FROM options op order by op.id asc");
		try {
			Query query = session.createQuery(hql.toString()) ;
			for(Iterator it = query.getResultList().iterator(); it.hasNext();) {
				 Option option = (Option) it.next();
				 OptionModel optionModel = new OptionModel();
				 optionModel.setId(option.getId());
				 optionModel.setName(option.getName());
				 optionModelList.add(optionModel);
			}
		} catch (Exception e) {
			LOGGER.error("ERROR at findAll(): ", e);
		}
		return optionModelList;
	}

}
