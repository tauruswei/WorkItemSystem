package com.firefly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.firefly.mapper.WorkitemMapper;
import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.service.WorkItemService;
import com.firefly.utils.PageUtils;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class WorkItemServiceImpl implements WorkItemService {

	@Autowired
	private WorkitemMapper workitemMapper;
	
	@Override
	public void saveWorkItem(Workitem workItem) throws Exception {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		workitemMapper.insert(workItem);
		
	}
	@Override
	public void updateWorkItem(Workitem workItem) {
		workitemMapper.updateByPrimaryKey(workItem);
	}
	@Override
	
	public void deleteWorkItem(String workItemId) {
		workitemMapper.deleteByPrimaryKey(workItemId);
	}

	@Override
	public Workitem queryWorkItemById(String workItemId) {
		
		Example example = new Example(Workitemdetail.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("questionid",workItemId);
		List<Workitem> workItemList = workitemMapper.selectByExample(example);
		return workItemList.get(0);
		
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public PageUtils queryWorkItemListPaged(Workitem workitem, Integer pageNum, Integer pageSize) {
		
		Example example = new Example(Workitem.class);
		Example.Criteria criteria = example.createCriteria();
		
		if((workitem.getQuestionname()!=null)&&!workitem.getQuestionname().equals("null")&&!workitem.getQuestionname().equals("")){
			criteria.andEqualTo("questionname",workitem.getQuestionname());
		}
		if(((workitem.getUsername()!=null))&&!workitem.getUsername().equals("null")&&!workitem.getUsername().equals("")){
			criteria.andEqualTo("username",workitem.getUsername());
		}
		if(((workitem.getWorkitemtype()!=null))&&!workitem.getWorkitemtype().equals("null")&&!workitem.getWorkitemtype().equals("")){
			criteria.andEqualTo("workitemtype",workitem.getWorkitemtype());
		}
		if((workitem.getStatus()!=null)&&!workitem.getStatus().equals("null")){
			criteria.andEqualTo("status",workitem.getStatus());
		}
		if(((workitem.getPerformer()!=null))&&!workitem.getPerformer().equals("null")){
			criteria.andEqualTo("performer",workitem.getPerformer());
		}
		
		example.setOrderByClause("updatedtime");
		List<Workitem> workItemTotal = workitemMapper.selectByExample(example);
		
		// 开始分页
        PageHelper.startPage(pageNum, pageSize);
//        example.setOrderByClause("updatedtime");
		List<Workitem> workItemList = workitemMapper.selectByExample(example);
		
		PageUtils pageObject = null;
		if(workItemList!=null){
			pageObject = new PageUtils(workItemList, workItemTotal.size(), pageSize, pageNum);
		}
		return pageObject;
	}

	@Override
	public List<Workitem> queryWorkItemList(Workitem workitem) {
		Example example = new Example(Workitem.class);
		Example.Criteria criteria = example.createCriteria();
		if((workitem.getQuestionname()!=null)&&!workitem.getQuestionname().equals("null")){
			criteria.andEqualTo("questionname",workitem.getQuestionname());
		}
		if((workitem.getStatus()!=null)&&!workitem.getStatus().equals("null")){
			criteria.andEqualTo("status",workitem.getStatus());
		}
		if(((workitem.getPerformer()!=null))&&!workitem.getPerformer().equals("null")){
			criteria.andEqualTo("performer",workitem.getPerformer());
		}
		if(((workitem.getUsername()!=null))&&!workitem.getUsername().equals("null")){
			criteria.andEqualTo("username",workitem.getUsername());
		}
		List<Workitem> workItemList = workitemMapper.selectByExample(example);
		return workItemList;
	}
}
