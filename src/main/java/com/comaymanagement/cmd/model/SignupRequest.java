package com.comaymanagement.cmd.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
	String username;
	String email;
	String password;
	Set<String> role;
}
