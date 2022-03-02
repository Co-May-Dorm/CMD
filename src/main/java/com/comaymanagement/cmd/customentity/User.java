package com.comaymanagement.cmd.customentity;

import java.util.Set;

import com.comaymanagement.cmd.entity.Position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private String username;
	private boolean enableLogin;
	
}
