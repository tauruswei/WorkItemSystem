package com.firefly.service;

import java.util.List;

import com.firefly.pojo.Workitem;
import com.firefly.utils.PageUtils;

public interface WorkItemService {

	public void saveWorkItem(Workitem workItem) throws Exception;

	public void updateWorkItem(Workitem workItem);

	public void deleteWorkItem(String workItemId);

	public Workitem queryWorkItemById(String workItemId);

	public List<Workitem> queryWorkItemList(Workitem workItem);

	public PageUtils queryWorkItemListPaged(Workitem workitem, Integer page, Integer pageSize);
}
