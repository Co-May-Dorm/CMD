package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.RoleModel;
import com.comaymanagement.cmd.repositoryimpl.RoleRepositoryImpl;

@Service
public class RoleService {
	@Autowired
	RoleRepositoryImpl roleRepository;
	
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<Object> findAll(String name, String sort, String order, String page) {
		page = page == null ? "1" : page.trim();
		name = name == null ? "" : name.trim();
		int limit = 15;
		// Caculator offset
		int offset = (Integer.parseInt(page) - 1) * limit;

		// Order by defaut
		if (sort == null || sort == "") {
			sort = "r.id";
		}
		if (order == null || order == "") {
			order = "desc";
		}
		try {
			List<RoleModel> roleModelList = roleRepository.findAll(name, sort, order , limit, offset);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(Integer.valueOf(page));
			pagination.setTotalItem(roleRepository.CountTotalItem());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("roles", roleModelList);
			results.put("pagination", pagination);
			if(roleModelList == null) {
				LOGGER.info("NOT FOUND");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Have error:","NOT FOUND",""));
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query produce successfully:",results));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR","Have error: ",e.getMessage()));
		}

	}


}
