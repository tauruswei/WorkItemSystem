package com.firefly.service;

import java.util.List;

import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemtype;
import com.firefly.utils.PageUtils;

public interface WorkItemTypeService {

	public List<Workitemtype> queryWorkItemTypeList();

}
