package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Task;
import com.comaymanagement.cmd.entity.TaskHis;
import com.comaymanagement.cmd.model.TaskModel;
import com.comaymanagement.cmd.repository.ITaskRepository;

@Repository
@Transactional(rollbackFor = Exception.class)
public class TaskRepositoryImpl implements ITaskRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<TaskModel> findByStatusId(String statusId, String sort, String order, Integer offset, Integer limit) {
		List<Task> taskList = new ArrayList<Task>();
		List<TaskModel> customTaskList = new ArrayList<TaskModel>();
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
			for (Task task : taskList) {
				PriorityQueue<Department> departmentList = new PriorityQueue<>(new TaskComparator());
				TaskModel customTask = new TaskModel();
				for(Department d : task.getCreator().getDepartments()) {
					departmentList.add(d);
				}
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(departmentList.remove().getName());
				customTask.setStatusName(task.getStatus().getName());

				customTaskList.add(customTask);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in findByStatusId() ", e);
		}

		return customTaskList;
	}

	@Override
	public List<TaskModel> findAll(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order, Integer offset, Integer limit) {
		Set<Task> taskSet = new LinkedHashSet<Task>();
		List<TaskModel> customTaskList = new ArrayList<TaskModel>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		hql.append("INNER JOIN c.departments as dep ");
		hql.append("WHERE dep.name LIKE CONCAT('%',:dep,'%') ");
		hql.append("AND t.title LIKE CONCAT('%',:title,'%') ");
		hql.append("AND s.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND c.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND r.name LIKE CONCAT('%',:receiver,'%') ");
		//hql.append("AND t.createDate LIKE CONCAT('%',:createDate,'%') ");
		//hql.append("AND t.finishDate LIKE CONCAT('%',:finishDate,'%') ");
		hql.append("order by t." + sort + " " + order );
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
//			LOGGER.info(hql.toString());
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
				taskSet.add(task);
			}
			for (Task task : taskSet) {
				PriorityQueue<Department> departmentList = new PriorityQueue<>(new TaskComparator());
				for(Department d : task.getCreator().getDepartments()) {
					departmentList.add(d);
				}
				TaskModel customTask = new TaskModel();
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(departmentList.remove().getName());
				customTask.setStatusName(task.getStatus().getName());
				customTaskList.add(customTask);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in findAll() ", e);
		}
		return customTaskList;
	}
	
	@Override
	public Integer countAllPaging(String dep, String title, String status, String creator, String receiver,
			String createDate, String finishDate, String sort, String order) {
		Integer count = 0;
		Set<Task> taskSet = new LinkedHashSet<Task>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("INNER JOIN t.creator as c ");
		hql.append("INNER JOIN t.status as s ");
		hql.append("INNER JOIN t.receiver as r ");
		hql.append("INNER JOIN c.departments as dep ");
		hql.append("WHERE dep.name LIKE CONCAT('%',:dep,'%') ");
		hql.append("AND t.title LIKE CONCAT('%',:title,'%') ");
		hql.append("AND s.name LIKE CONCAT('%',:status,'%') ");
		hql.append("AND c.name LIKE CONCAT('%',:creator,'%') ");
		hql.append("AND r.name LIKE CONCAT('%',:receiver,'%') ");
		//hql.append("AND t.createDate LIKE CONCAT('%',:createDate,'%') ");
		//hql.append("AND t.finishDate LIKE CONCAT('%',:finishDate,'%') ");
		hql.append("order by t." + sort + " " + order );
		
		try {
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

			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Object[] obj = (Object[]) it.next();
				Task task = (Task) obj[0];
				taskSet.add(task);
			}
			count = taskSet != null ? taskSet.size() : 0;
		} catch (Exception e) {
			LOGGER.error("Error has occured in countAll() ", e);
		}
		return count;
	}
	@Override
	public List<TaskModel> findByStatusIds(List<Integer> statusIds, String sort, String order, Integer offset,
			Integer limit) {
		List<Task> tasks = new ArrayList<Task>();
		List<TaskModel> customTasks = new ArrayList<TaskModel>();
		StringBuilder hql = new StringBuilder("FROM tasks AS t ");
		hql.append("WHERE status_id IN (:ids) ");
		hql.append("ORDER BY " + sort + " " + order);
		try {
			LOGGER.info(hql.toString());
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("ids", statusIds);
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			tasks = query.getResultList();
			for(Task task : tasks) {
				PriorityQueue<Department> departmentQueue = new PriorityQueue<>(new TaskComparator());
				for(Department d : task.getCreator().getDepartments()) {
					departmentQueue.add(d);
				}
				TaskModel customTask = new TaskModel();
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setDepartmentName(departmentQueue.remove().getName());
				customTask.setStatusName(task.getStatus().getName());
				customTasks.add(customTask);
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured in findByStatusIds() ", e);
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
	public Integer add(Task task) {
		try {
			LOGGER.info("SAVE TASK....");
			Session session = sessionFactory.getCurrentSession();
			return Integer.parseInt(session.save(task).toString());
		} catch (Exception e) {
			LOGGER.error("Error has occured in addEmployee() ", e);
			return -1;
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
	public TaskModel findById(Integer id) {
		TaskModel customTask = new TaskModel();
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
				PriorityQueue<Department> departmentList = new PriorityQueue<>(new TaskComparator());
				for(Department d : task.getCreator().getDepartments()) {
					departmentList.add(d);
				}
				customTask.setId(task.getId());
				customTask.setTitle(task.getTitle());
				customTask.setCreatorId(task.getCreator().getId());
				customTask.setCreatorName(task.getCreator().getName());
				customTask.setRecieverId(task.getReceiver().getId());
				customTask.setRecieverName(task.getReceiver().getName());
				customTask.setCreateDate(task.getCreateDate());
				customTask.setFinishDate(task.getFinishDate());
				customTask.setStatusName(task.getStatus().getName());
				customTask.setDepartmentName(departmentList.remove().getName());
				customTask.setDescription(task.getDescription());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return customTask;
	}
	
	// delete	
		public String deleteTaskById(Integer id) {
			Session session = sessionFactory.getCurrentSession();
			try {
				Task task = new Task();
				task = session.find(Task.class, id);
				TaskHis th = new TaskHis();
				th = session.find(TaskHis.class, id);
				if(th != null) {
					session.remove(th);
				}
				if(task!=null) {
					session.remove(task);
				}
				return "1";
			} catch (Exception e) {
				LOGGER.error("Error has occured in delete() ", e);
				return "0";
			}
		}
	//edit
		@Override
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
		// filter
		@Override
		public List<TaskModel> filter(String createFrom, String createTo, String finishFrom, String finishTo, String title,
				String creator, String receiver, String department, Integer limit, String order, String page,String sort) {
			List<Task> tasks = new ArrayList<Task>();
			List<TaskModel> customTasks = new ArrayList<TaskModel>();
			StringBuilder hql = new StringBuilder("FROM tasks AS ta");
			hql.append(" inner join ta.creator as em");
			hql.append(" inner join em.departments as de");
			hql.append(" inner join ta.receiver as em1");
			hql.append(" where em.name LIKE CONCAT('%',:creator,'%')");
			hql.append(" AND de.name LIKE CONCAT('%',:department,'%')");//department of creator
			hql.append(" AND em1.name LIKE CONCAT('%',:receiver,'%')");
			hql.append(" AND ta.title LIKE CONCAT('%',:title,'%')");
			if(createFrom.toString().length()>5) {
				hql.append(" and ta.createDate>=:createFrom");
			}
			if(createTo.toString().length()>5) {
				hql.append(" and ta.createDate<=:createTo");
			}
			if(finishFrom.toString().length()>5) {
				hql.append(" and ta.finishDate>=:finishFrom");
			}
			if(finishTo.toString().length()>5) {
				hql.append(" and ta.finishDate<=:finishTo");
			}
			hql.append(" ORDER BY ta."+sort+" "+order);
			try {
				Session session = sessionFactory.getCurrentSession();
				Query query = session.createQuery(hql.toString());
				query.setParameter("creator",creator);
				query.setParameter("receiver",receiver);
				query.setParameter("department",department);
				query.setParameter("title",title);
				if(createFrom.toString().length()>5) {
					query.setParameter("createFrom",createFrom);
				}
				if(createTo.toString().length()>5) {
					query.setParameter("createTo",createTo);
				}
				if(finishFrom.toString().length()>5) {
					query.setParameter("finishFrom",finishFrom);
				}
				if(finishTo.toString().length()>5) {
					query.setParameter("finishTo",finishTo);
				}
				LOGGER.info(hql.toString());
				int offset = (Integer.valueOf(page) - 1) * limit;
				query.setFirstResult(offset);
				query.setMaxResults(limit);
				for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
					Object[] obj = (Object[]) it.next();
					Task task = (Task) obj[0];
					tasks.add(task);
				}
				for(Task item : tasks) {
					TaskModel task = new TaskModel();
					task.setId(item.getId());
					task.setTitle(item.getTitle());
					task.setCreatorId(item.getCreator().getId());
					task.setCreatorName(item.getCreator().getName());
					task.setRecieverId(item.getReceiver().getId());
					task.setRecieverName(item.getReceiver().getName());
					task.setCreateDate(item.getCreateDate());
					task.setFinishDate(item.getFinishDate());
//					task.setDepartmentName(item.getCreator().getDepartment().getName());
					task.setStatusName(item.getStatus().getName());
					customTasks.add(task);
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			return customTasks;
		}
		@Override
		public Integer countFilter(String createFrom, String createTo, String finishFrom, String finishTo, String title,String creator, String receiver, String department) {
			StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM tasks AS ta");
			hql.append(" inner join ta.creator as em");
			hql.append(" inner join em.departments as de");
			hql.append(" inner join ta.receiver as em1");
			hql.append(" where em.name LIKE CONCAT('%',:creator,'%')");
			hql.append(" AND de.name LIKE CONCAT('%',:department,'%')");//department of creator
			hql.append(" AND em1.name LIKE CONCAT('%',:receiver,'%')");
			hql.append(" AND ta.title LIKE CONCAT('%',:title,'%')");
			if(createFrom.toString().length()>5) {
				hql.append(" and ta.createDate>=:createFrom");
			}
			if(createTo.toString().length()>5) {
				hql.append(" and ta.createDate<=:createTo");
			}
			if(finishFrom.toString().length()>5) {
				hql.append(" and ta.finishDate>=:finishFrom");
			}
			if(finishTo.toString().length()>5) {
				hql.append(" and ta.finishDate<=:finishTo");
			}
			int count =0;
			try {
				Session session = sessionFactory.getCurrentSession();
				Query query = session.createQuery(hql.toString());
				query.setParameter("creator",creator);
				query.setParameter("receiver",receiver);
				query.setParameter("department",department);
				query.setParameter("title",title);
				if(createFrom.toString().length()>5) {
					query.setParameter("createFrom",createFrom);
				}
				if(createTo.toString().length()>5) {
					query.setParameter("createTo",createTo);
				}
				if(finishFrom.toString().length()>5) {
					query.setParameter("finishFrom",finishFrom);
				}
				if(finishTo.toString().length()>5) {
					query.setParameter("finishTo",finishTo);
				}
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

class TaskComparator implements Comparator<Department>{
    // Overriding compare()method of Comparator 
                // for descending order of cgpa
    public int compare(Department d1, Department d2) {
    	if(d1.getLevel() < d2.getLevel()) {
			return 1;
		}else if(d1.getLevel() > d2.getLevel()) {
			return -1;
		}
		return 0;
    }
}
