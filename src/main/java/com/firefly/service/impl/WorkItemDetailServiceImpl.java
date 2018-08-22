package com.firefly.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.firefly.mapper.WorkitemdetailMapper;
import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.service.WorkItemDetailService;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class WorkItemDetailServiceImpl implements WorkItemDetailService {

	@Autowired
	private WorkitemdetailMapper workItemDetailMapper;
	

	@Override
	public void saveWorkItem(Workitemdetail workitemdetail) throws Exception {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		workItemDetailMapper.insert(workitemdetail);
	}
		
	@Override
	public void updateWorkItem(Workitemdetail workitemdetail) {
		workItemDetailMapper.updateByPrimaryKey(workitemdetail);
	}

	@Override
	public void deleteWorkItem(String workitemdetailId) {
		// TODO Auto-generated method stub
		workItemDetailMapper.deleteByPrimaryKey(workitemdetailId);
	}

	@Override
	public Workitemdetail queryWorkItemDetailById(String questionId) {
		
		Example example = new Example(Workitemdetail.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("questionid",questionId);
		List<Workitemdetail> workitemdetail = workItemDetailMapper.selectByExample(example);
		
		return workitemdetail.get(0);
	}

	@Override
	public List<Workitemdetail> queryWorkItemList(Workitemdetail workitemdetail) {
		try {
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Example example = new Example(Workitem.class);
		Example.Criteria criteria = example.createCriteria();
		
		List<Workitemdetail> userList = workItemDetailMapper.selectByExample(example);
		
		return userList;
	}
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<Workitemdetail> queryWorkItemDetailListPagedById(Integer pageNum, Integer pageSize,String questionId) {
		
		if(pageNum!=null&&pageSize!=null){
			// 开始分页
	        PageHelper.startPage(pageNum, pageSize);
		}
		
        Example example = new Example(Workitemdetail.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("questionid",questionId);
		example.setOrderByClause("updatedtime");
		List<Workitemdetail> workItemList = workItemDetailMapper.selectByExample(example);
		return workItemList;
	}

	@Override
	public List<Workitemdetail> queryWorkItemDetailByIdCustom(String questionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
