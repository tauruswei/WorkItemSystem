package com.firefly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.firefly.mapper.WorkitemtypeMapper;
import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.pojo.Workitemtype;
import com.firefly.service.WorkItemTypeService;
import com.firefly.utils.PageUtils;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class WorkItemTypeServiceImpl implements WorkItemTypeService {

	@Autowired
	private WorkitemtypeMapper workitemtypeMapper;
	
	@Override
	public List<Workitemtype> queryWorkItemTypeList() {
		Example example = new Example(Workitemtype.class);
		Example.Criteria criteria = example.createCriteria();
		List<Workitemtype> workItemTypeList = workitemtypeMapper.selectByExample(example);
		return workItemTypeList;
	}
}
