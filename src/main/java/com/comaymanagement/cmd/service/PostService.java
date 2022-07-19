package com.comaymanagement.cmd.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Department;
import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Post;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.DepartmentModel;
import com.comaymanagement.cmd.repository.IPostRepository;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class PostService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	IPostRepository postRepositoryImpl;
	@Autowired
	Message message;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);
	
	public ResponseEntity<Object> findAll(String title, String content, String sort, String order) {
		title = (title != null && !title.equals(""))? title : "";
		content = (content != null && !content.equals("")) ? content : "";
		List<Post> posts = new ArrayList<>();
		// Order by defaut
		if (sort == null || sort.equals("")) {
			sort = "post.createDate";
		}
		if (order == null || order.equals("")) {
			order = "desc";
		}
		posts = postRepositoryImpl.findAll(title, content,sort, order);
		if (posts.size() > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "", posts));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR", "Not found", posts));
		}
	}
	public ResponseEntity<Object> add(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectPost;
		try {
			UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			jsonObjectPost = jsonMapper.readTree(json);
			String title = jsonObjectPost.get("title") != null ? jsonObjectPost.get("title").asText() : "";
			String content = jsonObjectPost.get("content") != null ? jsonObjectPost.get("content").asText() : "";
			Boolean isPulished = jsonObjectPost.get("isPulished") != null ? jsonObjectPost.get("isPulished").asBoolean() :true;
			Integer createBy = userDetail.getId();
			Integer modifyBy = userDetail.getId();
			String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			String modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			Post post = new Post();
			post.setTitle(title);
			post.setContent(content);
			post.setPulished(isPulished);
			post.setCreateBy(createBy);
			post.setCreateDate(createDate);
			post.setModifyBy(modifyBy);
			post.setModifyDate(modifyDate);
			Integer status =  postRepositoryImpl.add(post);
			if (status != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK",message.getMessageByItemCode("POSTS1") , post));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR",message.getMessageByItemCode("POSTE1"), post));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at add() ", e);
			LOGGER.error(json);
			return null;
		}
	}
	public ResponseEntity<Object> edit(String json) {
		JsonMapper jsonMapper = new JsonMapper();
		JsonNode jsonObjectPost;
		try {
			UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			jsonObjectPost = jsonMapper.readTree(json);
			String id = jsonObjectPost.get("id") != null ? jsonObjectPost.get("id").asText() : "";
			String title = jsonObjectPost.get("title") != null ? jsonObjectPost.get("title").asText() : "";
			String content = jsonObjectPost.get("content") != null ? jsonObjectPost.get("content").asText() : "";
			Boolean isPulished = jsonObjectPost.get("isPulished") != null ? jsonObjectPost.get("isPulished").asBoolean() :true;
			Integer modifyBy = userDetail.getId();
			String modifyDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime());
			Post post = postRepositoryImpl.findById(Integer.valueOf(id));
			post.setTitle(title);
			post.setContent(content);
			post.setPulished(isPulished);
			post.setModifyBy(modifyBy);
			post.setModifyDate(modifyDate);
			Integer status =  postRepositoryImpl.edit(post);
			if (status != -1) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK",message.getMessageByItemCode("POSTS2") , post));
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ERROR",message.getMessageByItemCode("POSTE2"), post));
			}
		} catch (Exception e) {
			LOGGER.error("Error has occured at edit() ", e);
			LOGGER.error(json);
			return null;
		}
	}
	public ResponseEntity<Object> delete(Integer id){
		Post postDelete = (Post)  postRepositoryImpl.findById(id);
		String deleteStatus = postRepositoryImpl.delete(id);
		try {
			if (deleteStatus.equals("1")) {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("POSTS3"), ""));
		} else {
				return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("ERROR", message.getMessageByItemCode("POSTS3"), ""));

			}
		} catch (Exception e) {
			LOGGER.error("Has error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("ERROR", e.getMessage(), ""));
			}
		}
	
}
