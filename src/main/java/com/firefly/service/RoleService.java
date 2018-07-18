package com.firefly.service;

import java.util.List;

import com.firefly.pojo.Role;
import com.firefly.pojo.Workitem;

public interface RoleService {

	public List<Role> queryRoleList(Role role);
	
	public void updateRole(Role role);
}
