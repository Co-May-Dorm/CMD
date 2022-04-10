package com.comaymanagement.cmd.repositoryimpl;

import java.sql.SQLException;
import java.sql.Statement;
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
import com.comaymanagement.cmd.entity.TaskHis;
import com.comaymanagement.cmd.repository.ITaskRepository;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

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
	public List<CustomTaskAll> findAll(String dep, String title, String status, String creator, String receiver,
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
		//hql.append("AND t.createDate LIKE CONCAT('%',:createDate,'%') ");
		//hql.append("AND t.finishDate LIKE CONCAT('%',:finishDate,'%') ");

		hql.append("order by t." + sort + " " + order );

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
			//query.setParameter("createDate", createDate);
			//query.setParameter("finishDate", finishDate);

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
	public Integer countAll(String dep, String title, String status, String creator, String receiver) {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		hql.append("WHERE c.department.name LIKE CONCAT('%',:dep,'%') ");
		hql.append("AND t.title LIKE CONCAT('%',:title,'%') ");
		hql.append("AND s.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND c.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND r.name LIKE CONCAT('%',:receiver,'%') ");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("dep", dep);
			query.setParameter("title", title);
			query.setParameter("status", status);
			query.setParameter("creator", creator);
			query.setParameter("receiver", receiver);
			LOGGER.info(hql.toString());
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			count = Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

	@Override
	public List<CustomTaskAll> findByStatusIds(List<String> statusIds, String sort, String order, String page,
			Integer limit) {
		List<Task> tasks = new ArrayList<Task>();
		List<CustomTaskAll> customTasks = new ArrayList<CustomTaskAll>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("WHERE status_id IN (:ids) ");
		hql.append("ORDER BY " + sort + " " + order);
		try {
			int offset = (Integer.valueOf(page) - 1) * limit;
			LOGGER.info(hql.toString());
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("ids", statusIds);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
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
	public Integer countFindByIds(List<Integer> ids) {
		Integer count = null;
		StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM tasks AS t ");
		hql.append("WHERE status_id IN (:ids)");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("ids", ids);
			LOGGER.info(hql.toString());
			@SuppressWarnings("rawtypes")
			List list = query.getResultList();
			count = Integer.parseInt(list.get(0).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return count;
	}

	@Override
	public Integer save(Task task) {
		try {
			LOGGER.info("SAVE TASK....");
			Session session = sessionFactory.getCurrentSession();
			return Integer.parseInt(session.save(task).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Integer getMaxId() {
		StringBuilder hql = new StringBuilder("SELECT t.id FROM tasks AS t ");
		hql.append("INNER JOIN t.creator ");
		hql.append("INNER JOIN t.status ");
		hql.append("INNER JOIN t.receiver ");
		hql.append("order by t.id DESC");
		try {
			LOGGER.info(hql.toString());
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setMaxResults(1);
			List tasks = query.getResultList();
			return Integer.parseInt(tasks.get(0).toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public CustomTaskAll findById(Integer id) {
		CustomTaskAll customTask = new CustomTaskAll();
		StringBuilder hql = new StringBuilder("FROM tasks AS ta ");
		hql.append(" inner join ta.creator as em");
		hql.append(" inner join ta.receiver as em1");
		hql.append(" inner join ta.status as st");
		hql.append(" WHERE ta.id = :id");
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			query.setParameter("id",id);			
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] obj = (Object[]) it.next();
				Task task = (Task) obj[0];
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setStatusName(task.getStatus().getName());
				customTask.setDepartmentName(task.getCreator().getDepartment().getName());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return customTask;
	}
	
	//
	@Override
	@Transactional
	public List<CustomTaskAll> sortByStatusIds(Integer statusId,String page) {
		List<CustomTaskAll> customTaskList = new ArrayList<CustomTaskAll>();
		List<Task> taskList = new ArrayList<Task>();
		//Integer id = ParseInt(statusId);
		StringBuilder hql = new StringBuilder("FROM tasks AS ta ");
		hql.append(" inner join ta.receiver as em");
		hql.append(" inner join ta.status as st");
		hql.append(" inner join ta.creator as cr");
		//hql.append(" WHERE ta.status = "+statusId);
		hql.append(" WHERE ta.status = :statusId");

		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			LOGGER.info(hql.toString());
			LOGGER.info(statusId.toString());
			query.setParameter("statusId",statusId);
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
			LOGGER.error(e.getMessage()+"Loi");
		}
		return customTaskList;
	}
	// delete	
		@Transactional
		public String deleteTaskById(Integer id) {
			Session session = sessionFactory.getCurrentSession();
			try {
				Task task = new Task();
				task = session.find(Task.class, id);
				TaskHis th = new TaskHis();
				th = session.find(TaskHis.class, id);
				session.remove(th);
				session.remove(task);
				return "1";
			} catch (Exception e) {
				LOGGER.error("Error has occured in delete() ", e);
				return "0";
			}
		}
	//edit
		@Override
		@Transactional
		public Integer edit(Task task) {
			Session session = sessionFactory.getCurrentSession();
			try {
				session.update(task);
				return 1;
			} catch (Exception e) {
				LOGGER.error("Error has occured in edit task ", e);
				return 0;
			}
		}
}
