	package com.comaymanagement.cmd.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comaymanagement.cmd.constant.CrossOriginConstant;
import com.comaymanagement.cmd.security.jwt.AuthenticationFilter;
import com.comaymanagement.cmd.security.jwt.JwtUtils;
import com.comaymanagement.cmd.service.CustomRoleService;
import com.comaymanagement.cmd.service.DepartmentService;
import com.comaymanagement.cmd.service.UserDetailsServiceImpl;
/**
All option name
	todolist
	request
	type
	employee
	department
	position
	inventory
	team
All permission name
 	view
 	create
 	update
 	detele
 	view_all
 	update_all
 	delete_all
 **/
@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = {CrossOriginConstant.REACT_ORIGIN,CrossOriginConstant.REACT_ORIGIN_LOCAL})
public class DepartmentController {
	@Autowired
	DepartmentService departmentService;
	@Autowired
    private JwtUtils jwtUtils;
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
	
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	@PreAuthorize("@customRoleService.canView('department', principal) or @customRoleService.canViewAll('department', principal)")
	@GetMapping("")
	public ResponseEntity<Object> findAll(HttpServletRequest request, @RequestParam(value = "name", required = false) String name) {
		 String token = CustomRoleService.getTokenFromRequest(request);
         UserDetails userDetails = null;
         if (token != null && jwtUtils.validateJwtToken(token)) {
             String username = jwtUtils.getUserNameFromJwtToken(token);
             try {
             	userDetails = userDetailsService.loadUserByUsername(username);
				} catch (Exception e) {
					logger.error("",e);
				}
             UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                     userDetails, null, userDetails.getAuthorities());
             authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(authentication);
         }
		return departmentService.findAll(name);
	}
//	@PreAuthorize("@customRoleService.canView('department',principal)")
//	@GetMapping("")
//	public ResponseEntity<Object> findAllWithFindOnly(@RequestParam(value = "name", required = false) String name) {
//		return departmentService.findAll(name);
//	}
	@PreAuthorize("@customRoleService.canCreate('department',principal)")
	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody String json){
		return departmentService.add(json);
	}
	
	@PreAuthorize("@customRoleService.canUpdate('department',principal) or @customRoleService.canUpdateAll('department', principal)")
	@PutMapping("/edit")
	public ResponseEntity<Object> edit(@RequestBody String json){
		return departmentService.edit(json);
	}

	@PreAuthorize("@customRoleService.canDelete('department',principal) or @customRoleService.canDeleteAll('department', principal)")
	@DeleteMapping(value = "/delete/{id}")
	@ResponseBody
	public ResponseEntity<Object> delete(@PathVariable Integer id){
		return departmentService.delete(id);
	}
}
