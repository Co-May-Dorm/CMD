package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.customentity.CustomRoleAll;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.entity.Role;
import com.comaymanagement.cmd.repositoryimpl.RoleRepositoryImpl;

@Service
public class RoleService implements IGeneralService<Role> {
	@Autowired
	RoleRepositoryImpl roleRepository;
	
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<Object> findAllRole(String sort, String order, String page) {
		order = order == null ? "r.unique_number" : order.trim();
		int limit = 15;
		sort = sort == null ? "DESC" : sort.trim();
		page = page == null ? "1" : page.trim();
		try {
			List<CustomRoleAll> customRoleAlls = roleRepository.findAllRole(sort, order, page,limit);
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage(page);
			pagination.setTotalItem(roleRepository.CountTotalItem());
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("taskList", customRoleAlls);
			results.put("Pagination", pagination);
			if(customRoleAlls == null) {
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

	@Override
	public Optional<Role> findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role save(Role t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Role model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Role> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
