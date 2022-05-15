package com.comaymanagement.cmd.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import com.comaymanagement.cmd.entity.ResponseObject;
import com.comaymanagement.cmd.model.LoginRequest;
import com.comaymanagement.cmd.model.User;
import com.comaymanagement.cmd.repository.UserRepository;
import com.comaymanagement.cmd.security.jwt.JwtUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthService {
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
}
