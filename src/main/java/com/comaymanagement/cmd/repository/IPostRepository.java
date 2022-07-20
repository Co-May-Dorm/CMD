package com.comaymanagement.cmd.repository;

import java.util.List;

import com.comaymanagement.cmd.entity.Position;
import com.comaymanagement.cmd.entity.Post;

public interface IPostRepository {
	public List<Post> findAll(String title, String content, String sort, String order);
	public Integer add(Post post);
	public Integer edit(Post post);
	public Post findById(Integer id);
	public String delete(Integer id);
}
