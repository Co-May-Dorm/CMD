package com.comaymanagement.cmd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.comaymanagement.cmd.constant.CMDConstrant;
import com.comaymanagement.cmd.entity.Pagination;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.ProposalModel;
import com.comaymanagement.cmd.repositoryimpl.ProposalRepositoryImpl;

@Service
public class ProposalService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ProposalRepositoryImpl proposalRepositoryImpl;
	
	public ResponseEntity<Object> findAll(String proposal, String content, String status, String creator,
			String createDate, String finishDate, String sort, String order, Integer limit, String page) {
		content = content == null ? "" : content.trim();
		status = status == null ? "" : status.trim();
		creator = creator == null ? "" : creator.trim();
		proposal = proposal == null ? "" : proposal.trim();
		createDate = createDate == null ? "" : createDate.trim();
		finishDate = finishDate == null ? "" : finishDate.trim();
		order = order == null ? "DESC" : order;
		sort = sort == null ? "pro.id" : sort;
		page = page == null ? "1" : page.trim();
		limit = CMDConstrant.LIMIT;
		
		int offset = (Integer.parseInt(page) - 1) * limit;

		// Order by defaut
		if (sort == null || sort == "") {
			sort = "pro.id";
		}
		if (order == null || order == "") {
			order = "desc";
		}
		try {
			List<ProposalModel> proposalModels = new ArrayList<ProposalModel>();
			proposalModels = proposalRepositoryImpl.findAll(proposal, content, status, creator, createDate, finishDate, sort, order, offset, limit);
			Integer totalProposal  = 0;
			totalProposal = proposalRepositoryImpl.countAllPaging(proposal, content, status, creator, createDate, finishDate, sort, order, offset, limit);
			
			Pagination pagination = new Pagination();
			pagination.setLimit(limit);
			pagination.setPage( Integer.valueOf(page));
			pagination.setTotalItem(totalProposal);
			
			Map<String, Object> results = new TreeMap<String, Object>();
			results.put("pagination", pagination);
			results.put("proposals", proposalModels);
			
			if (results.size() >  0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("OK", "Query produce successfully: ", results));
			} else {
				pagination.setPage(1);
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Not found", "Not found", results));

			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR", e.getMessage(), ""));
		}
	}
}
