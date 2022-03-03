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

import com.comaymanagement.cmd.customentity.CustomTaskAll;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repository.ITaskRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public class TaskRepositoryImpl implements ITaskRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Task> findByStatusId(String statusId,String sort,String order,Integer limit,Integer offset) {
		List<Task> taskList = new ArrayList<Task>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("WHERE t.status.id = :statusId");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("statusId", statusId);
			
			query.setFirstResult(offset);
			query.setMaxResults(limit);

			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object obj = (Object) it.next();
				Task task = (Task) obj;		
				taskList.add(task);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		
		return taskList;
	}

	@Override
	public List<Task> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, Integer limit, Integer offset) {
		List<Task> taskList = new ArrayList<Task>();
		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		hql.append("WHERE c.department.name LIKE CONCAT('%',:dep,'%') ");
		hql.append("AND t.title LIKE CONCAT('%',:title,'%') ");
		hql.append("AND s.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND c.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND r.name LIKE CONCAT('%',:receiver,'%') ");
		/*hql.append("AND t.createDate LIKE CONCAT('%',:createDate,'%') ");
		hql.append("AND t.finishDate LIKE CONCAT('%',:finishDate,'%') ");*/
		hql.append("order by " + order + " " + sort);

		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("dep", dep);
			query.setParameter("title", title);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("receiver", receiver);
			/*query.setParameter("createDate", createDate);
			query.setParameter("finishDate", finishDate);*/
			query.setFirstResult(offset);
			query.setMaxResults(limit);

			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] obj = (Object[]) it.next();
				Task task = (Task) obj[0];
				/*Employee employeeCreated = (Employee) obj[1];
				Status taskStatus = (Status)obj[2];
				Employee employeeReceived = (Employee) obj[3];*/		
				taskList.add(task);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return taskList;
	}

}
