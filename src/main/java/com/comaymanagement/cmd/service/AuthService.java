package com.comaymanagement.cmd.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.comaymanagement.cmd.constant.Message;
import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.LoginRequest;
import com.comaymanagement.cmd.model.User;
import com.comaymanagement.cmd.repository.UserRepository;
import com.comaymanagement.cmd.repositoryimpl.EmployeeRepositoryImpl;
import com.comaymanagement.cmd.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	   @Autowired
	   AuthenticationManager authenticationManager;
	 
	   @Autowired
	   PasswordEncoder encoder;

	   @Autowired
	   JwtUtils jwtUtils;
	
	   @Autowired
	   UserRepository userRepository;
	   
	   @Autowired 
	   Message message;
	   
	   @Autowired 
	   EmployeeRepositoryImpl employeeRepository;
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
		User user = userRepository.findByUsername(loginRequest.getUsername());
		if(user == null) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("ERROR",message.getMessageByItemCode("LOGINE2") ,""));
		}
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if(userDetails == null ) {
        	
        }
			
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", message.getMessageByItemCode("LOGINS1"),jwt));
	}
	
	public ResponseEntity<Object> changePassword(String json){
		UserDetailsImpl userDetail = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JsonMapper jsonMapper = new JsonMapper();
		Integer empId = null;
		 String existingPassword = null;
		 String newPassword = null;
		try {
			   JsonNode jsonObject = jsonMapper.readTree(json);
			    empId = jsonObject.get("empId").asInt();
			    existingPassword = jsonObject.get("existingPassword").asText();
			    newPassword = jsonObject.get("newPassword").asText();
		} catch (Exception e) {
			LOGGER.error("Have error at changePassword();", e);
		}
	 
		if(encoder.matches( existingPassword,userDetail.getPassword())) {
			String newPasswordEncoder = encoder.encode(newPassword);
			userDetail.setPassword(newPasswordEncoder);
			Employee employee = employeeRepository.findById(empId);
			employee.setPassword(newPasswordEncoder);
			employeeRepository.edit(employee);
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Cập nhật mật khẩu thành công",""));
	}
}
