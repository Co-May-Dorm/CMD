package com.comaymanagement.cmd.repositoryimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.entity.Post;
import com.comaymanagement.cmd.repository.IPostRepository;
@Repository
@Transactional(rollbackFor = Exception.class)
public class PostRepositoryImpl implements IPostRepository{
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Post> findAll(String title, String content, String sort, String order) {
		StringBuilder hql = new StringBuilder();
		List<Post> posts = new ArrayList<>();
		hql.append("from posts post ");
		hql.append("where post.title like CONCAT('%',:title,'%') ");
		hql.append("and post.content like CONCAT('%',:content,'%') ");
		hql.append("order by " + sort + " " + order);
		Session session = this.sessionFactory.getCurrentSession();
		try {
			Query query = session.createQuery(hql.toString());
			query.setParameter("title", title);
			query.setParameter("content", content);
			LOGGER.info(hql.toString());
			for (Iterator it = query.getResultList().iterator(); it.hasNext();) {
				Post post = (Post) it.next();
				posts.add(post);
			}
			return posts;
		} catch (Exception e) {
			LOGGER.error("Error has occured in findAll() ", e);
			return null;
		}
	}
	@Override
	public Post findById(Integer id) {
		Post post = new Post();
		StringBuilder hql = new StringBuilder();
		hql.append("FROM posts post ");
		hql.append("WHERE post.id = :id");
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(hql.toString());
			query.setParameter("id", id);
			post  = (Post) query.getSingleResult();
			return post;
		} catch (Exception e) {
			LOGGER.error("Error has occured in findById() ", e);
			return null;
		}
		
		
	}
	@Override
	public Integer add(Post post) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Integer id = (Integer) session.save(post);
			return id;
		} catch (Exception e) {
			LOGGER.error("Error has occured at add() ", e);
		}
		return -1;
	}

	@Override
	public Integer edit(Post post) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.update(post);
			return 1;
		} catch (Exception e) {
			LOGGER.error("Error has occured at edit() ", e);
			return 0;
		}
	}
	@Override
	public String delete(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Post post = session.find(Post.class, id);
			session.remove(post);
			return "1";
		} catch (Exception e) {
			LOGGER.error("Error has occured in delete() ", e);
			return "0";
		}
	}

}
