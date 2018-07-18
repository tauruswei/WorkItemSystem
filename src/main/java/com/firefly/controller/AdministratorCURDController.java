package com.firefly.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.firefly.pojo.Role;
import com.firefly.pojo.Workitem;
import com.firefly.pojo.Workitemdetail;
import com.firefly.service.RoleService;
import com.firefly.service.WorkItemDetailService;
import com.firefly.service.WorkItemService;
import com.firefly.utils.PageUtils;
import com.firefly.utils.ResultUtil;
import com.firefly.utils.SASUtil;
import com.firefly.utils.WorkItemUtil;

@Controller
@RequestMapping("/firefly")
public class AdministratorCURDController {
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private WorkItemService workItemService;

	@Autowired
	private WorkItemDetailService workItemDetailService;

	@Autowired
	private Sid sid;

	public SASUtil sasUtil;
	
	/**
	 * 后台管理员首页
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public String Login() {
		return "thymeleaf/login";
	}
	@RequestMapping(value = "/login1")
	public String Login1() {
		return "thymeleaf/login1";
	}
	
	@RequestMapping(value = "/adminIndex",method=RequestMethod.POST)
	public String adminIndex(HttpServletRequest request,Model model) {

		//1.接收参数
  		String username = request.getParameter("username");
  		String passwd = request.getParameter("passwd");
  		
  		//封装成Role对象
  		Role role = new Role();
  		role.setUsername(username);
  		role.setPasswd(passwd);
		List<Role> list = roleService.queryRoleList(role);
		
		if(list.size()>0){
			model.addAttribute("username", username);
			return "thymeleaf/index";
		}else{
			return "redirect:/firefly/login1";
		}
	}
<<<<<<< HEAD
	// 修改密码跳转页面
	@RequestMapping(value= "/modifyPasswordPage")
	public String modifyPasswordPage() {
		
		return "thymeleaf/modifyPassword";
	}
=======
	
>>>>>>> 880eaf497f27aa2a2ef3a4172ac76098d05e416f
	// 修改密码
	@RequestMapping(value= "/modifyPassword", method = RequestMethod.POST)
	public String modifyPassword(HttpServletRequest request,Model model) {
		//1.接收参数
  		String userName = request.getParameter("userName");
  		String oldPasswd = request.getParameter("oldPasswd");
  		String newPasswd = request.getParameter("newPasswd");
		
  		//封装成Role对象
  		Role role = new Role();
  		role.setUsername(userName);
  		role.setPasswd(oldPasswd);
		List<Role> list = roleService.queryRoleList(role);
		
		if(list.size()>0){
			role.setPasswd(newPasswd);
			roleService.updateRole(role);
<<<<<<< HEAD
			return "thymeleaf/modifyPasswordSuccess";
		}else{
			return "thymeleaf/modifyPasswordFail";
		}
	}
	
	// 修改密码success
	@RequestMapping(value= "/modifyPasswordSuccess", method = RequestMethod.POST)
	public String modifyPasswordSuccess(HttpServletRequest request,Model model) {
		//1.接收参数
		String userName = request.getParameter("userName");
		String oldPasswd = request.getParameter("oldPasswd");
		String newPasswd = request.getParameter("newPasswd");
		
		//封装成Role对象
		Role role = new Role();
		role.setUsername(userName);
		role.setPasswd(oldPasswd);
		List<Role> list = roleService.queryRoleList(role);
		
		if(list.size()>0){
			role.setPasswd(newPasswd);
			roleService.updateRole(role);
=======
>>>>>>> 880eaf497f27aa2a2ef3a4172ac76098d05e416f
			return "redirect:/modifyPasswordSuccess.html";
		}else{
			return "redirect:/modifyPasswordFail.html";
		}
	}
	@RequestMapping(value = "/unsolvedQuestions",method=RequestMethod.GET)
	public String unsolvedQuestions(@RequestParam(value = "questionName",required = false) String questionname,
			@RequestParam(value = "userName", required = false)String userName,
			@RequestParam(value = "status",   required = false, defaultValue = "open") String status, 
			@RequestParam(value = "performer",required = false, defaultValue = "user") String performer,
			@RequestParam(value = "pageNum",  required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
			@RequestParam(value = "workItemType", required = false) String workItemType,
			Model model) throws Exception {

		if (status.equals("open")) {
			Workitem workitem = new Workitem();
			workitem.setStatus(status);
			workitem.setPerformer("admin");
			List<Workitem> workItemList11 = workItemService.queryWorkItemList(workitem);
			for (int i = 0; i < workItemList11.size(); i++) {
				if (WorkItemUtil.getDiffDate(workItemList11.get(i).getUpdatedtime(), Calendar.DAY_OF_MONTH) >= 2) {
					Workitem workItem = workItemList11.get(i);
					workItem.setStatus("closed");
					workItem.setPerformer("admin");
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					workItem.setUpdatedtime(time);
					workItemService.updateWorkItem(workItem);

					// 向workItemDetail表中插入一条数据，作为结束问题的描述
					Workitemdetail workItemDetail = new Workitemdetail();
					workItemDetail.setId(sid.nextShort());
					workItemDetail.setQuestionid(workItem.getQuestionid());
					workItemDetail.setUsername(workItem.getUsername());
					workItemDetail.setQuestionname(workItem.getQuestionname());
					workItemDetail.setDescription("由于您48小时之内没有操作，该问题自动关闭，感谢您对firefly钱包的支持！！");
					workItemDetail.setPerformer("system");
					workItemDetail.setUpdatedtime(time);
					workItemDetailService.saveWorkItem(workItemDetail);
				}
			}
		}
		
		Workitem workitem = new Workitem();
		workitem.setQuestionname(questionname);
		workitem.setUsername(userName);
		workitem.setStatus(status);
		
		if(!status.equals("closed")){
			workitem.setPerformer(performer);
		}
		if(workItemType!= null){
			workitem.setWorkitemtype(workItemType);
		}
		PageUtils pageObject = workItemService.queryWorkItemListPaged(workitem, Integer.parseInt(pageNum), Integer.parseInt(pageSize));

		model.addAttribute("pageObject", pageObject);
		
		if (pageObject.getList().size()>0) {
			return "thymeleaf/unsolvedQuestions";
		} else {
			return "thymeleaf/unsolvedQuestionsNull";
		}
		
	}
	@RequestMapping(value = "/solvedQuestions",method=RequestMethod.GET)
	public String solvedQuestions(@RequestParam(value = "questionName",required = false) String questionname,
			@RequestParam(value = "userName", required = false)String username,
			@RequestParam(value = "status",   required = false, defaultValue = "closed") String status, 
			@RequestParam(value = "performer",required = false) String performer,
			@RequestParam(value = "pageNum",  required = false, defaultValue = "1") String pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") String pageSize,
			@RequestParam(value = "workItemType", required = false) String workItemType,
			Model model) throws Exception {
		
		Workitem workitem = new Workitem();
		workitem.setQuestionname(questionname);
		workitem.setUsername(username);
		workitem.setStatus(status);
		workitem.setWorkitemtype(workItemType);
		
		PageUtils pageObject = workItemService.queryWorkItemListPaged(workitem, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		
		model.addAttribute("pageObject", pageObject);
		
		if (pageObject.getList().size()>0) {
			return "thymeleaf/solvedQuestions";
		} else {
			return "thymeleaf/solvedQuestionsNull";
		}
	}
	

	/**
	 * 后台管理员对用户的问题进行回复，修改description
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateWorkItemDetail", method = RequestMethod.POST)
	public String updateWorkItemDetail(HttpServletRequest request) throws Exception {
		
		// 用户每次更新description，相当于在表workItemDetail中插入了一条数据
		Workitemdetail workItemDetail = new Workitemdetail();
		
		workItemDetail.setId(sid.nextShort());
		workItemDetail.setUsername(request.getParameter("username"));
		workItemDetail.setPerformer("admin");
		workItemDetail.setDescription(request.getParameter("description"));
		workItemDetail.setQuestionid(request.getParameter("questionid"));
		workItemDetail.setQuestionname(request.getParameter("questionname"));
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		workItemDetail.setUpdatedtime(time);
		workItemDetailService.saveWorkItem(workItemDetail);

		Workitem workItem = workItemService.queryWorkItemById(workItemDetail.getQuestionid());
		workItem.setPerformer("admin");
		workItem.setUpdatedtime(time);
		workItemService.updateWorkItem(workItem);
		return "redirect:/firefly/queryWorkItemById?questionid="+request.getParameter("questionid");
	}
	
	@RequestMapping("/queryWorkItemById")
	public String queryWorkItemById(@RequestParam(value = "questionid") String questionid,
			@RequestParam(value = "pageNum", required = false) String pageNum,
			@RequestParam(value = "pageSize", required = false) String pageSize,
			Model model) {
		Workitem workItem = workItemService.queryWorkItemById(questionid);
		List<Workitemdetail> workItemDetailList = new ArrayList<>();
		if(pageNum!=null&&pageSize!=null){
			workItemDetailList = workItemDetailService.queryWorkItemDetailListPagedById(Integer.parseInt(pageNum), Integer.parseInt(pageSize),
			questionid);
		}else{
			workItemDetailList = workItemDetailService.queryWorkItemDetailListPagedById(null,null,questionid);
		}
		if(workItemDetailList.size()>0){
			
			for(int i=0;i<workItemDetailList.size();i++){
				String fileName = null;
				if(workItemDetailList.get(i).getFilename()!=null){
					fileName = sasUtil.getSASforRead(workItemDetailList.get(i).getFilename());
					workItemDetailList.get(i).setFilename(fileName);
				}
			}
			if(workItemDetailList.size()>1){
				if(workItemDetailList.get(workItemDetailList.size()-1).getDescription()==null){
					model.addAttribute("workItemDetailList", workItemDetailList.subList(1, workItemDetailList.size()-1));
					if(workItemDetailList.get(workItemDetailList.size()-2).getDescription().indexOf("关闭")>-1){
						model.addAttribute("descriptionlast", "由于您48小时之内没有操作，该问题自动关闭，感谢您对firefly钱包的支持！！");
					}else{
						model.addAttribute("descriptionlast", workItemDetailList.get(workItemDetailList.size()-2).getDescription());
					}
					model.addAttribute("evaluatedetail", workItemDetailList.get(workItemDetailList.size()-1).getEvaluatedetail());
					model.addAttribute("updatedtime", workItemDetailList.get(workItemDetailList.size()-1).getUpdatedtime());
				}else{
					model.addAttribute("workItemDetailList", workItemDetailList.subList(1, workItemDetailList.size()));
					if(workItemDetailList.get(workItemDetailList.size()-1).getDescription().indexOf("关闭")>-1){
						model.addAttribute("descriptionlast", "由于您48小时之内没有操作，该问题自动关闭，感谢您对firefly钱包的支持！！");
					}else{
						model.addAttribute("descriptionlast", workItemDetailList.get(workItemDetailList.size()-1).getDescription());
					}
				}
				
			}
			model.addAttribute("status", workItem.getStatus());
			model.addAttribute("questionname",workItemDetailList.get(0).getQuestionname());
			model.addAttribute("username",workItemDetailList.get(0).getUsername());
			model.addAttribute("createdtime",workItemDetailList.get(0).getUpdatedtime());
			model.addAttribute("descriptionfirst",workItemDetailList.get(0).getDescription());
			model.addAttribute("filename",workItemDetailList.get(0).getFilename());
			model.addAttribute("questionid", questionid);
		}
		 return "thymeleaf/workItemDetailForAdmin";
	}
	// 关闭问题
	@RequestMapping("/closeWorkItem")
	public String closeWorkItem(@RequestParam(value = "questionId") String questionid) throws Exception {

		Workitem workItem = workItemService.queryWorkItemById(questionid);
		if(workItem==null) return "没有该问题";
		
		workItem.setStatus("closed");
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		workItem.setUpdatedtime(time);
		workItemService.updateWorkItem(workItem);
		
		// 向workItemDetail表中插入一条数据，作为结束问题的描述
		Workitemdetail workItemDetail = new Workitemdetail();
		workItemDetail.setId(sid.nextShort());
		workItemDetail.setQuestionid(workItem.getQuestionid());
		workItemDetail.setUsername(workItem.getUsername());
		workItemDetail.setQuestionname(workItem.getQuestionname());
		workItemDetail.setDescription("该问题已经解决，因此管理员关闭了该问题，感谢您对firefly钱包的支持！！");
		workItemDetail.setPerformer("admin");
		workItemDetail.setUpdatedtime(time);
		workItemDetailService.saveWorkItem(workItemDetail);
		return "redirect:/firefly/unsolvedQuestions?status=open";
	}


	// 管理员下载文件
	//path 是服务器存放图片的位置    比如：E:/
//	@RequestMapping(value = "download", method = RequestMethod.GET)
//	public void downLoad(@RequestParam(value = "workItemId") String workItemId,
//			@RequestParam(value = "path") String path, HttpServletResponse response) {
//		String filename = WorkItemUtil.getFileFullName(path, workItemId);
//		File file = new File(path + "/" + filename);
//		if (file.exists()) { // 判断文件父目录是否存在
//			response.setContentType("application/force-download");
//			response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
//
//			byte[] buffer = new byte[1024];
//			FileInputStream fis = null; // 文件输入流
//			BufferedInputStream bis = null;
//
//			OutputStream os = null; // 输出流
//			try {
//				os = response.getOutputStream();
//				fis = new FileInputStream(file);
//				bis = new BufferedInputStream(fis);
//				int i = bis.read(buffer);
//				while (i != -1) {
//					os.write(buffer);
//					i = bis.read(buffer);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			try {
//				bis.close();
//				fis.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
