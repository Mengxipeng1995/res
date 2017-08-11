package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.SystemLogsDAO;
import com.cmp.res.entity.SystemLogs;
import com.cmp.res.util.CommonUtil;


@Service
public class SystemLogsService {
	
	@Autowired
	private SystemLogsDAO systemLogsDAO;
	
	
	
	public Page<SystemLogs> searchSyslogs(Integer pn,Integer ps){
		PageRequest pageRequest = CommonUtil.buildPageRequest(pn, ps, null, null, null);
		return systemLogsDAO.findAll(pageRequest);
	}
	

}
