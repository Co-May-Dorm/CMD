package com.comaymanagement.cmd.customentity;

import java.util.List;

import com.comaymanagement.cmd.entity.Employee;
import com.comaymanagement.cmd.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTaskAll {
	private Employee creator;
	private List<Employee> recieverList;
	private Status status;
	private String title;
	private String description;
	private String createDate;
}
