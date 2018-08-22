package com.firefly.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.firefly.pojo.Result;
import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.pojo.Workitemtype;
import com.firefly.service.WorkItemDetailService;
import com.firefly.service.WorkItemService;
import com.firefly.service.WorkItemTypeService;
import com.firefly.utils.PageUtils;
import com.firefly.utils.ResultUtil;
import com.firefly.utils.WorkItemUtil;

@RestController
@RequestMapping("user")
public class UserCURDController {

	@Autowired
	private WorkItemService workItemService;

	@Autowired
	private WorkItemDetailService workItemDetailService;

	@Autowired
	private WorkItemTypeService workItemTypeService;

	@Autowired
	private Sid sid;
	
	private static Logger logger = Logger.getLogger(UserCURDController.class);
	
	public RestTemplate restTemepalte = new RestTemplate();

	/**
	 * 用户首页
	 */
	@RequestMapping(value = "/userIndex", method = RequestMethod.GET)
	public String userIndex(HttpSession session) {
		Assertion ass = AssertionHolder.getAssertion();
		AttributePrincipalImpl p = (AttributePrincipalImpl) ass.getPrincipal();
		String userName = (String) p.getAttributes().get("email");
		return userName;
	}

	/**
	 * 测试
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(@RequestParam(value = "userName") String userName, @RequestParam(value = "time") Long time,
			HttpServletRequest request, Model model) throws Exception {

		Boolean result = WorkItemUtil.verify(userName, time, request);
		
		System.out.println(result);

		// 创建workitem对象
		return "thymeleaf/createWorkItem";
	}

	/**
	 * 用户创建一个问题
	 */
	@RequestMapping(value = "/createWorkItem", method = RequestMethod.GET)
	public String createWorkItem(@RequestParam(value = "userName") String userName, Model model) throws Exception {
		// 创建workitem对象
		Workitem workItem = new Workitem();
		workItem.setUsername(userName);
		model.addAttribute("workItem", workItem);

		return "thymeleaf/createWorkItem";
	}
	
	/**
	 * SAS数据,用来上传文件
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getSASforUpload", method = RequestMethod.GET)
	public Result getSASforUpload(@RequestParam(value = "userName",required=false) String userName,
		@RequestParam(value = "time",required=false) Long time,
		HttpServletRequest request) {
		//exception     用来判断获取sas是否成功
		Result result = null;
		if (WorkItemUtil.verify(userName, time, request)) {
			try {
				String url = "http://139.219.8.252:33604/sas/cont/ticket";
				
				HttpHeaders headers = new HttpHeaders();
				
				headers.add("x-fchain-sas", "ja3NmwQqVXLbbmAqt3yk4XzZ");
				
				HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
				
				ResponseEntity<String> response = restTemepalte.exchange(url, HttpMethod.GET, requestEntity, String.class);
				
				String sas = response.getBody();
				
				result = ResultUtil.success("获取上传文件的SAS成功", sas);
			} catch (Exception e) {
				result = ResultUtil.error("获取上传文件的SAS失败");
			} 
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}
	/**
	 * SAS数据,用来读取文件
	 */
//	@RequestMapping(value = "/getSASforRead", method = RequestMethod.GET)
//	public Result getSASforRead(@RequestParam(value = "userName",required=false) String userName,
//			@RequestParam(value = "time",required=false) Long time,
//			@RequestParam(value = "fileName",required=false) String fileName,
//			HttpServletRequest request) throws Exception {
//		//exception     用来判断获取sas是否成功
//		Result result = null;
//		if (WorkItemUtil.verify(userName, time, request)) {
//			
//			String[] fileNameArray = fileName.split(";");
//			StringBuffer buffer = new StringBuffer();
//			for(int i=0;i<fileNameArray.length;i++){
//				
//				String url = "http://139.219.8.252:33604/sas/blob/ticket-test/"+fileNameArray[i];
//				
//				HttpHeaders headers = new HttpHeaders();
//				headers.add("x-fchain-sas", "ja3NmwQqVXLbbmAqt3yk4XzZ");
//				
//				HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
//				
//				ResponseEntity<String> response = restTemepalte.exchange(url, HttpMethod.GET, requestEntity, String.class);
//				
//				String sttr ="https"+response.getBody().split("https")[1].split("\"}")[0];
//				
////				System.out.println(sttr);
////				System.out.println(sttr.split("{\"store_url\": \"")[1].split("\"}")[0]);
////				System.out.println("https"+sttr.split("https")[1].split("\"}")[0]);
//				
////				urlArray.add(sttr);	
//				buffer.append(sttr);
//				buffer.append(";");
//			}
//			System.out.println(buffer.toString());
//			result = ResultUtil.success("获取读取文件的SAS成功", buffer.toString());
//		} else {
//			result = ResultUtil.error("获取读取文件的SAS失败");
//		}
//		return result;
//	}
	
	/**
	 * SAS数据,用来读取文件
	 */
	public String getSASforRead(String fileName){
		String[] fileNameArray = fileName.split(";");
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<fileNameArray.length;i++){
			
			String url = "http://139.219.8.252:33604/sas/blob/ticket/"+fileNameArray[i];
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("x-fchain-sas", "ja3NmwQqVXLbbmAqt3yk4XzZ");
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
			
			ResponseEntity<String> response = restTemepalte.exchange(url, HttpMethod.GET, requestEntity, String.class);
			
			String sttr ="https"+response.getBody().split("https")[1].split("\"}")[0];
			
//			System.out.println(sttr);
//			System.out.println(sttr.split("{\"store_url\": \"")[1].split("\"}")[0]);
//			System.out.println("https"+sttr.split("https")[1].split("\"}")[0]);
			
//			urlArray.add(sttr);	
			buffer.append(sttr);
//			buffer.append(";");
		}
		return buffer.toString();
	}
//	//put请求
//	public static String httpPut(String urlPath, String data, String charSet, String[] header)
//	{
//		String result = null;
//		URL url = null;
//		HttpURLConnection httpurlconnection = null;
//		try
//		{
//			url = new URL(urlPath);
//			httpurlconnection = (HttpURLConnection) url.openConnection();
//			httpurlconnection.setDoInput(true);
//			httpurlconnection.setDoOutput(true);
//			httpurlconnection.setConnectTimeout(2000000);// 设置连接主机超时（单位：毫秒）
//			httpurlconnection.setReadTimeout(2000000);// 设置从主机读取数据超时（单位：毫秒）
// 
//			if (header != null)
//			{
//				for (int i = 0; i < header.length; i++)
//				{
//					String[] content = header[i].split(":");
//					httpurlconnection.setRequestProperty(content[0], content[1]);
//				}
//			}
// 
//			httpurlconnection.setRequestMethod("PUT");
//			httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
// 
//			if (StringUtils.isNotBlank(data))
//			{
////				httpurlconnection.getOutputStream().write(data.getBytes("UTF-8"));
//				httpurlconnection.getOutputStream().write(data.getBytes("UTF-8"));
//			}
//			httpurlconnection.getOutputStream().flush();
//			httpurlconnection.getOutputStream().close();
//			int code = httpurlconnection.getResponseCode();
//			String str = httpurlconnection.getResponseMessage();
//			System.out.println(str);
//			if (code == 200)
//			{
//				DataInputStream in = new DataInputStream(httpurlconnection.getInputStream());
//				int len = in.available();
//				byte[] by = new byte[len];
//				in.readFully(by);
//				if (StringUtils.isNotBlank(charSet))
//				{
//					result = new String(by, Charset.forName(charSet));
//				} else
//				{
//					result = new String(by);
//				}
//				in.close();
//			} else
//			{
//				logger.error("请求地址：" + urlPath + "返回状态异常，异常号为：" + code);
//			}
//		} catch (Exception e)
//		{
//			logger.error("访问url地址：" + urlPath + "发生异常", e);
//		} finally
//		{
//			url = null;
//			if (httpurlconnection != null)
//			{
//				httpurlconnection.disconnect();
//			}
//		}
//		return result;
//	}

	
	/**
	 * 保存问题
	 */
	@RequestMapping(value = "/saveWorkItem", method = RequestMethod.POST)
	public Result saveWorkItem(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "workItemType") String workItemType,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "questionName", required = false) String questionname,
			@RequestParam(value = "fileName", required = false) String fileName,
//			@RequestParam(value = "time") Long time, DefaultMultipartHttpServletRequest multipartRequest, HttpServletRequest request) throws Exception {
			@RequestParam(value = "time") Long time,
			HttpServletRequest request) throws Exception {
		// public void saveworkItem( consumes = "multipart/form-data",
		// HttpServletRequest request) throws Exception {
		Result result = null;
		request.setCharacterEncoding("UTF-8");
		if (WorkItemUtil.verify(userName, time, request)) {
//			// 1. 文件上传工厂
//			FileItemFactory factory = new DiskFileItemFactory();
//			// 2. 创建文件上传核心工具类
//			ServletFileUpload upload = new ServletFileUpload(factory);
//
//			// 一、设置单个文件允许的最大的大小： 5M
//			upload.setFileSizeMax(5 * 1024 * 1024);
//			// 二、设置文件上传表单允许的总大小: 80M
//			upload.setSizeMax(80 * 1024 * 1024);
//			// 三、 设置上传表单文件名的编码
//			// 相当于：request.setCharacterEncoding("UTF-8");
//			upload.setHeaderEncoding("UTF-8");
			// 将数据添加到workitem表中
			try {
				Workitem workItem = new Workitem();
				String workItemId = sid.nextShort();
				workItem.setQuestionid(workItemId);
				workItem.setUsername(userName);
				workItem.setQuestionname(questionname);
				workItem.setDescription(description);
				workItem.setWorkitemtype(workItemType);
				String time1 = WorkItemUtil.time();
				workItem.setCreatedtime(time1);
				workItem.setUpdatedtime(time1);
				workItem.setPerformer("user");
				workItem.setStatus("open");
				workItemService.saveWorkItem(workItem);
				
				// 将数据添加到workItemDetail表中
				Workitemdetail workItemDetail = new Workitemdetail();
				workItemDetail.setId(sid.nextShort());
				workItemDetail.setQuestionid(workItemId);
				workItemDetail.setUsername(workItem.getUsername());
				workItemDetail.setQuestionname(workItem.getQuestionname());
				workItemDetail.setDescription(workItem.getDescription());
				workItemDetail.setPerformer(workItem.getPerformer());
				workItemDetail.setUpdatedtime(workItem.getCreatedtime());

				 // 可以处理多个文件上传功能
//			 List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
//			 List<MultipartFile> files =((AbstractMultipartHttpServletRequest) request).getFiles("file");
//			 MultipartFile file = null;
//			 BufferedOutputStream stream = null;
//			 String fileName = null;
//			 for (int i = 0; i < files.size(); ++i) {
//				 file = files.get(i);
//				 if (!file.isEmpty()) {
//					 // 普通文本数据
//					 String name = file.getOriginalFilename();// 文件名
//					 // workItemId与文件名拼接
//					 fileName = workItemId + "#" + name;
//					 // 获取上传基路径
//					 String path = "E:";
//					 // 创建目标文件
//					 byte[] bytes = file.getBytes();
//					 stream = new BufferedOutputStream(new FileOutputStream(new File(path,fileName)));
//					 stream.write(bytes);
//					 stream.close();
//				 }
//				 workItemDetail.setFilepath("http://dapp.stellar.cash:8081/myres/"+fileName+";");
					 workItemDetail.setFilename(fileName);
//			 }
				workItemDetailService.saveWorkItem(workItemDetail);
				result = ResultUtil.success("创建工单成功", null);
			} catch (Exception e) {
				result = ResultUtil.error("创建工单失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}

		// return "thymeleaf/submitSuccess";
		return result;
	}

	@RequestMapping(value = "/queryWorkItemDetailById", method = RequestMethod.GET)
	public Result queryWorkItemDetailById(@RequestParam(value = "questionId") String questionid,
			@RequestParam(value = "userName") String userName, @RequestParam(value = "time") Long time,
			@RequestParam(value = "workItemType", required = false) String workItemType,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize, HttpServletRequest request) {
		Result result = null;
		if (WorkItemUtil.verify(userName, time, request)) {
			try {
				List<Workitemdetail> workItemDetailList = new ArrayList<>();
				if (pageNum != null && pageSize != null) {
					workItemDetailList = workItemDetailService.queryWorkItemDetailListPagedById(Integer.parseInt(pageNum),
							Integer.parseInt(pageSize), questionid);
				} else {
					workItemDetailList = workItemDetailService.queryWorkItemDetailListPagedById(null, null, questionid);
				}
				if(workItemDetailList.size()>0){
					if(workItemDetailList.get(0).getUsername()!=null && !(workItemDetailList.get(0).getUsername().equals(userName))){
						return ResultUtil.error("用户名不一致，该用户不可以查看该工单");
					}
					for(int i=0;i<workItemDetailList.size();i++){
						String fileName = null;
						if(workItemDetailList.get(i).getFilename()!=null){
							fileName = getSASforRead(workItemDetailList.get(i).getFilename());
							workItemDetailList.get(i).setFilename(fileName);
						}
					}
				}
				result = ResultUtil.success("查询工单对话详细信息成功", workItemDetailList);
			} catch (Exception e) {
				result = ResultUtil.error("查询工单对话详细信息失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}

	@RequestMapping(value = "/queryWorkItems", method = RequestMethod.GET)
	public Result queryWorkItems(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "performer", required = false) String performer,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
			@RequestParam(value = "time") Long time,
			@RequestParam(value = "workItemType", required = false) String workItemType, HttpServletRequest request)
			throws Exception {
		Result result = null;
		if (WorkItemUtil.verify(userName, time, request)) {
			try {
				if ((status != null)&&(status.equals("open"))) {
					Workitem workitem = new Workitem();
					workitem.setUsername(userName);
					workitem.setStatus(status);
					workitem.setPerformer("admin");
					List<Workitem> workItemList11 = workItemService.queryWorkItemList(workitem);
					for (int i = 0; i < workItemList11.size(); i++) {
						if (WorkItemUtil.getDiffDate(workItemList11.get(i).getUpdatedtime(), Calendar.DAY_OF_MONTH) >= 2) {
							Workitem workItem = workItemList11.get(i);
							workItem.setStatus("closed");
							workItem.setPerformer("admin");
							String time1 = WorkItemUtil.time();
							workItem.setUpdatedtime(time1);
							workItemService.updateWorkItem(workItem);

							// 向workItemDetail表中插入一条数据，作为结束问题的描述
							Workitemdetail workItemDetail = new Workitemdetail();
							workItemDetail.setId(sid.nextShort());
							workItemDetail.setQuestionid(workItem.getQuestionid());
							workItemDetail.setUsername(workItem.getUsername());
							workItemDetail.setQuestionname(workItem.getQuestionname());
							workItemDetail.setDescription("由于您48小时之内没有操作，该问题自动关闭，感谢您对firefly钱包的支持！！");
							workItemDetail.setPerformer("system");
							workItemDetail.setUpdatedtime(time1);
							workItemDetailService.saveWorkItem(workItemDetail);
						}
					}
				}
				Workitem workitem = new Workitem();
				workitem.setUsername(userName);
				if (status != null) {
					workitem.setStatus(status);
				}
				if (performer != null) {
					workitem.setStatus(performer);
				}
				if (workItemType != null) {
					workitem.setWorkitemtype(workItemType);
				}
				PageUtils pageObject = workItemService.queryWorkItemListPaged(workitem, Integer.parseInt(pageNum),
						Integer.parseInt(pageSize));
				result = ResultUtil.success("查询工单成功", pageObject);
			} catch (Exception e) {
				result = ResultUtil.error("查询工单失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}

	// 关闭问题
	@RequestMapping("/closeWorkItem")
	public Result closeWorkItem(@RequestParam(value = "questionId") String questionid,
			@RequestParam(value = "userName") String userName, @RequestParam(value = "time") Long time,
			HttpServletRequest request) throws Exception {
		Result result = null;
		if (WorkItemUtil.verify(userName, time, request)) {

			try {
				Workitem workItem = workItemService.queryWorkItemById(questionid);
				
				if((userName!=null)&&(!userName.equals(workItem.getUsername()))){
					result = ResultUtil.error("用户名和工单的用户名不一致");
					return result;
				}

				workItem.setStatus("closed");
				workItemService.updateWorkItem(workItem);

				// 向workItemDetail表中插入一条数据，作为结束问题的描述
				Workitemdetail workItemDetail = new Workitemdetail();
				workItemDetail.setId(sid.nextShort());
				workItemDetail.setQuestionid(workItem.getQuestionid());
				workItemDetail.setUsername(workItem.getUsername());
				workItemDetail.setQuestionname(workItem.getQuestionname());
				workItemDetail.setDescription("感谢您对firefly钱包的支持！！");
				workItemDetail.setPerformer("system");
				String time1 = WorkItemUtil.time();
				workItem.setUpdatedtime(time1);
				workItemDetail.setUpdatedtime(time1);
				workItemDetailService.saveWorkItem(workItemDetail);
				result = ResultUtil.success("关闭工单成功", null);
			} catch (Exception e) {
				result = ResultUtil.error("关闭工单失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}

	// 获取所有的工单类型
	@RequestMapping("/queryWorkItemTypeList")
	public Result queryWorkItemTypeList() {
		Result result = null;
		List<Workitemtype> workItemTypeList = workItemTypeService.queryWorkItemTypeList();
		if (workItemTypeList.size() > 0) {
			result = ResultUtil.success("获取工单类型成功", workItemTypeList);
		} else {
			result = ResultUtil.error("获取工单类型失败");
		}
		return result;
	}

	// 用户评价
	@RequestMapping(value = ("/evaluateWorkItem"), method = RequestMethod.POST)
	public Result evaluateWorkItem(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "time") Long time,
			@RequestParam(value = "questionId") String questionId,
			@RequestParam(value = "evaluateScore") String evaluateScore,
			@RequestParam(value = "evaluateDetail", required = false) String evaluateDetail,
			HttpServletRequest request) {
		Result result = null;
		if (WorkItemUtil.verify(userName, time, request)) {
			try {
				
				Workitem workItem = workItemService.queryWorkItemById(questionId);
				Workitemdetail workItemDetail = new  Workitemdetail();
				
				if((userName!=null)&&(!userName.equals(workItem.getUsername()))){
					result = ResultUtil.error("用户名和工单的用户名不一致");
					return result;
				}
				
				
				workItemDetail.setId(sid.nextShort());
				workItemDetail.setQuestionid(questionId);
				workItemDetail.setPerformer("user");
				String time1 = WorkItemUtil.time();
				workItemDetail.setUpdatedtime(time1);
				workItemDetail.setUsername(userName);
				workItemDetail.setQuestionname(workItem.getQuestionname());	
				workItemDetail.setEvaluatedetail(evaluateDetail);
				workItemDetailService.saveWorkItem(workItemDetail);
				
				workItem.setStatus("closed");
				workItem.setUpdatedtime(time1);
				workItem.setEvaluatescore(evaluateScore);
				workItem.setEvaluatedetail(evaluateDetail);
				workItemService.updateWorkItem(workItem);
				
				result = ResultUtil.success("用户评价成功", null);
			} catch (Exception e) {
				result = ResultUtil.error("用户评价失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}

	/**
	 * 用户修改特定问题的description，进行回复
	 */
	@RequestMapping(value = "/updateWorkItemDetail", method = RequestMethod.POST)
	public Result updateWorkItemDetail(@RequestBody Workitemdetail workItemDetail,
			HttpServletRequest request) throws Exception {
	
		Result result = null;
		if (WorkItemUtil.verify(workItemDetail.getUsername(), Long.parseLong(workItemDetail.getTime()), request)) {
	
			try {
				Workitem workItem = workItemService.queryWorkItemById(workItemDetail.getQuestionid());
				
				if((workItemDetail.getUsername()!=null)&&(!workItemDetail.getUsername().equals(workItem.getUsername()))){
					result = ResultUtil.error("用户名和工单的用户名不一致");
					return result;
				}
				
				// 用户每次更新description，相当于在表workItemDetail中插入了一条数据
				workItemDetail.setId(sid.nextShort());
				// workItemDetail.setUsername(request.getParameter("username"));
				workItemDetail.setPerformer("user");
				// workItemDetail.setDescription(request.getParameter("description"));
				String time1 = WorkItemUtil.time();
				workItemDetail.setUpdatedtime(time1);
				workItemDetailService.saveWorkItem(workItemDetail);
	
				// 修改workitem表中 questionId = workItem.getQuestionid()问题的performer
				
				workItem.setPerformer("user");
				workItem.setUpdatedtime(time1);
				workItemService.updateWorkItem(workItem);
				result = ResultUtil.success("更新工单对话详细信息成功", null);
			} catch (Exception e) {
				result = ResultUtil.error("更新工单对话详细信息失败");
			}
		} else {
			result = ResultUtil.error("签名验证没有通过或者时间超过5分钟");
		}
		return result;
	}
}
