package com.firefly.service;

import java.util.List;

import com.firefly.pojo.Workitemdetail;

public interface WorkItemDetailService {

	public void saveWorkItem(Workitemdetail workitemdetail) throws Exception;
	
	public void updateWorkItem(Workitemdetail workitemdetail);

	public void deleteWorkItem(String workItemId);

	public Workitemdetail queryWorkItemDetailById(String questionId);

	public List<Workitemdetail> queryWorkItemList(Workitemdetail workitemdetail);

	public List<Workitemdetail> queryWorkItemDetailListPagedById(Integer pageNum, Integer pageSize,String questionId);
	
	public List<Workitemdetail> queryWorkItemDetailByIdCustom(String questionId);
//	
//	public void saveworkItemTransactional(Worklist workItem);
}
