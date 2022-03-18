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
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.repository.ITaskRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public class TaskRepositoryImpl implements ITaskRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public List<CustomTaskAll> findByStatusId(String statusId, String sort, String order, String page, Integer limit) {
		List<Task> taskList = new ArrayList<Task>();
		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("WHERE t.status.id = :statusId");
		try {
			int offset = (Integer.valueOf(page) - 1) * limit;
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
			for (Task task : taskList) {
				CustomTaskAll customTask = new CustomTaskAll();
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(task.getCreator().getDepartment().getName());
				customTask.setStatusName(task.getStatus().getName());

				customTaskList.add(customTask);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return customTaskList;
	}

	@Override
	@Transactional
	public List<CustomTaskAll> findAllTask(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, String page, Integer limit) {

		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
		List<Task> taskList = new ArrayList<Task>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		hql.append("WHERE c.department.name LIKE CONCAT('%',:dep,'%') ");
		hql.append("AND t.title LIKE CONCAT('%',:title,'%') ");
		hql.append("AND s.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND c.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND r.name LIKE CONCAT('%',:receiver,'%') ");
		hql.append("AND t.createDate LIKE CONCAT('%',:createDate,'%') ");
		hql.append("AND t.finishDate LIKE CONCAT('%',:finishDate,'%') ");

		hql.append("order by " + order + " " + sort);

		try {
			int offset = (Integer.valueOf(page) - 1) * limit;
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("dep", dep);
			query.setParameter("title", title);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("receiver", receiver);
			query.setParameter("createDate", createDate);
			query.setParameter("finishDate", finishDate);

			query.setFirstResult(offset);
			query.setMaxResults(limit);
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] obj = (Object[]) it.next();
				Task task = (Task) obj[0];
				taskList.add(task);
			}
			for (Task task : taskList) {
				CustomTaskAll customTask = new CustomTaskAll();
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(task.getCreator().getDepartment().getName());
				customTask.setStatusName(task.getStatus().getName());
				customTaskList.add(customTask);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return customTaskList;
	}
	
	@Override
	public Integer CountTotalItemTaskAll() {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			count = Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomTaskAll> findByStatusIds(List<String> statusIds, String sort, String order, String page,
			Integer limit) {
		List<Task> tasks = new ArrayList<Task>();
		List<CustomTaskAll> customTasks = new ArrayList<CustomTaskAll>();
		StringBuilder hql = new StringBuilder("SELECT * FROM tasks AS t ");
		hql.append("WHERE status_id IN (?1)");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter(1, statusIds);
			tasks = query.getResultList();
			for(Task item : tasks) {
				CustomTaskAll task = new CustomTaskAll();
				task.setId(item.getId());
				task.setTitle(item.getTitle());
				task.setCreatorId(item.getCreator().getId());
				task.setCreatorName(item.getCreator().getName());
				task.setRecieverId(item.getReceiver().getId());
				task.setRecieverName(item.getReceiver().getName());
				task.setCreateDate(item.getCreateDate());
				task.setFinishDate(item.getFinishDate());
				task.setDepartmentName(item.getCreator().getDepartment().getName());
				task.setStatusName(item.getStatus().getName());
				customTasks.add(task);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return customTasks;
	}
	@Override
	public Integer CountTotalItemFindByIds() {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM tasks AS t ");
		hql.append("WHERE status_id IN (?1)");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			count = Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

}
