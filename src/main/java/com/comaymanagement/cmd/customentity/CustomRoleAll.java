package com.comaymanagement.cmd.customentity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomRoleAll {
	private String id;
	private String name;
	private List<CustomPositionAll> positions;
}
