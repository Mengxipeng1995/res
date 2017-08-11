package com.cmp.res.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.modules.mapper.JsonMapper;

import com.cmp.res.entity.SystemLogs;
import com.cmp.res.service.SystemLogsService;



@Controller
@RequestMapping("/syslogs/")
public class SystemLogsController {
	@Autowired
	private SystemLogsService systemLogsService;
	

	
	@RequestMapping("search")
	public void serarch(
			@RequestParam(value = "page", required = false,defaultValue="1") Integer pn,
			@RequestParam(value = "limit", required = false,defaultValue="20") Integer ps,
			HttpServletResponse response) throws IOException{
		
		Page<SystemLogs> entrys=systemLogsService.searchSyslogs(pn,ps);
		response.setContentType("text/html;charset=utf-8");
		response.getWriter()
		.write(JsonMapper.nonEmptyMapper().toJson(entrys));
		
	}

}
