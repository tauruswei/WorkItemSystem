package com.firefly.config;

import org.apache.log4j.Logger;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.service.WorkItemDetailService;
import com.firefly.service.WorkItemService;
import com.firefly.utils.WorkItemUtil;

@Component
public class RedisExpiredListener implements MessageListener {

	@SuppressWarnings("deprecation")
	private static final Logger log = Logger.getLogger("Log4jInActonTest.class");

	@Autowired
	private WorkItemDetailService workItemDetailService;
	
	@Autowired
	private WorkItemService workItemService;
	
	@Autowired
	private Sid sid;

	public final static String LISTENER_PATTERN = "__key*__:*";

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String questionId = new String(message.getBody());
//		System.out.println(questionId);
		Workitem workItem = workItemService.queryWorkItemById(questionId);
		
		Workitemdetail workItemDetail = new Workitemdetail();
		
		workItemDetail.setId(sid.nextShort());
		workItemDetail.setUsername(workItem.getUsername());
		workItemDetail.setPerformer("system");
		workItemDetail.setDescription("由于您48小时之内没有操作，该问题自动关闭，感谢您对firefly钱包的支持！！");
		workItemDetail.setQuestionid(questionId);
		workItemDetail.setQuestionname(workItem.getQuestionname());
		String time = WorkItemUtil.time();
		workItemDetail.setUpdatedtime(time);
		try {
			workItemDetailService.saveWorkItem(workItemDetail);
			workItem.setPerformer("admin");
			workItem.setUpdatedtime(time);
			workItem.setStatus("closed");
			workItemService.updateWorkItem(workItem);
			log.info("工单[" + questionId + "]超时关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
