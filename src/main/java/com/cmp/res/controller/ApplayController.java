package com.cmp.res.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cmp.res.entity.ApplayResourceMapping;
import com.cmp.res.entity.Apply;
import com.cmp.res.entity.Resource;
import com.cmp.res.entity.ReturnJson;
import com.cmp.res.entity.User;
import com.cmp.res.service.ApplayResourceMappingService;
import com.cmp.res.service.ApplyService;
import com.cmp.res.service.CommonService;
import com.cmp.res.service.Log4jDBAppender;
import com.cmp.res.service.ResourceService;
import com.cmp.res.shiro.ShiroDBRealm;
import com.cmp.res.util.CommonUtil;
import com.cmp.res.util.Log4jUtils;

@Controller
@RequestMapping("/apply/")
public class ApplayController {
	@Autowired
	private ApplyService applyService;
	@Autowired
	private ApplayResourceMappingService applayResourceMappingService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private CommonService commonService;
	
	public static Logger logger = LoggerFactory.getLogger(ApplayController.class);
	
	/**
	 * 发起申请
	 */
	@RequestMapping("launch")
	public void launchApplication(
			@RequestParam(value = "validDate",required=false) String validDate,
			@RequestParam(value = "resourceId",required=false) Long[] ids,
			@RequestParam(value = "describe",required=false) String describe,
			HttpServletResponse response
			){
		ReturnJson rj=new ReturnJson();
		User user=commonService.getCurrentLogin();
		
		if(ids!=null&&ids.length>0){
			Apply apply=new  Apply();
			apply.setApplicantId(user.getId());
			apply.setApplicantNickName(user.getNickName());
			apply.setApplicantUserName(user.getUserName());
			apply.setStatus(0);
			apply.setCreateDate(new Date());
			try{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				apply.setValidDate(sdf.parse(validDate));
			}catch (Exception e) {
				// TODO: handle exception
			}
			apply.setDescribeInfo(describe);
			
			applyService.save(apply);
			
			
			StringBuffer resourceStr=new StringBuffer();
			for(Long id:ids){
				Resource rsource=resourceService.findById(id);
				if(rsource!=null){
					ApplayResourceMapping arm=new ApplayResourceMapping();
					arm.setApplayId(apply.getId());
					arm.setResourceId(id);
					arm.setResourceName(rsource.getName());
					arm.setUniqueIdentifierString(rsource.getUniqueIdentifierString());
					arm.setTypeName(rsource.getTypeName());
					applayResourceMappingService.save(arm);
					resourceStr.append(rsource.getTypeName()).append(":").append(rsource.getName()).append(";");
				}
				
			}
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					user.getUserName(), 1, apply.getId(), resourceStr.toString()) ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"资源申请");
		}else{
			rj.setSuccess(false);
			rj.setMsg("请勾选申请资源");
		}
		commonService.returnDate(response, rj);
		
		
		
		
		
	}
	@RequestMapping("getApply")
	public void getApply(
			@RequestParam(value = "id") Long id,
			HttpServletResponse response
			){
		Map<String,Object> map=new HashMap<String,Object>();
		
		Apply apply=applyService.findById(id);
		
		List<ApplayResourceMapping> arms=applayResourceMappingService.findByApplayId(id);
		map.put("apply", apply);
		map.put("arms", arms);
		
		commonService.returnDate(response, map);
		
	}
	@RequestMapping("passApply")
	public void passApply(@RequestParam(value = "id") Long id,
			@RequestParam(value = "status",required=false) Integer status,
			@RequestParam(value = "responseText",required=false) String responseText,
			HttpServletResponse response){
		ReturnJson rj=new ReturnJson();
		try{
			Apply apply=applyService.findById(id);
			User user=commonService.getCurrentLogin();
			apply.setApproverId(user.getId());
			apply.setApproverNickName(user.getNickName());
			apply.setApproverUserName(user.getUserName());
			apply.setStatus(status);
			apply.setResponseText(responseText);
			applyService.save(apply);
			
			
			logger.info(Log4jDBAppender.EXTENT_MSG_BIT,Log4jUtils.getLogString(Log4jDBAppender.EXTENT_MSG_PROP_DIVIDER,
					user.getUserName(), 1, apply.getId(), "审核状态:"+status+"") ,
					Log4jDBAppender.EXTENT_MSG_DIVIDER,"资源审核");
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rj.setSuccess(false);
			rj.setMsg("保存失败");
		}
		
		commonService.returnDate(response, rj);
		
		
	}
	@RequestMapping("list")
	public void list(
			@RequestParam(value = "page",required=false,defaultValue="1") int pn,
			@RequestParam(value = "ps",required=false,defaultValue="20") int ps,
			HttpServletResponse response){
		String userName=commonService.getCurrentLogin().getUserName();
		//判断当前用户是否用户
		if(SecurityUtils.getSubject().hasRole("auditor")||"admin".equals(userName)){
			userName=null;
		}
		commonService.returnDate(response, applyService.outline(userName,null,pn, ps));
		
	}
	
	
	

}
