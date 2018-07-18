package com.firefly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.firefly.mapper.RoleMapper;
import com.firefly.pojo.Role;
import com.firefly.pojo.Workitem;
import com.firefly.service.RoleService;

import tk.mybatis.mapper.entity.Example;
@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> queryRoleList(Role role) {
		Example example = new Example(Role.class);
//		List<Role> roleList = roleMapper.selectByExample(example);
		Example.Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("username",role.getUsername());
		criteria.andEqualTo("passwd",role.getPasswd());
		List<Role> roleList = roleMapper.selectByExample(example);
//		List<Role> roleList = roleMapper.selectAll();
		
		return roleList;
	}

	@Override
	public void updateRole(Role role) {
		roleMapper.updateByPrimaryKey(role);
		
	}
}
