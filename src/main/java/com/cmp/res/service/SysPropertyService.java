package com.cmp.res.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.SysPropertyDAO;
import com.cmp.res.entity.SysProperty;
import com.cmp.res.util.CommonUtil;
import com.google.common.collect.Maps;

@Service
public class SysPropertyService implements BaseService<SysProperty>{
	
	@Autowired
	private SysPropertyDAO sysPropertyDAO;
	
	Map<String,String> sysPro=Maps.newHashMap();
	
	
	public  Page<SysProperty> findAll(){
		return sysPropertyDAO.findAll(CommonUtil.buildPageRequest(1, 1000, null, null, null));
	}
	
	public String getValue(String key){
		String value=sysPro.get(key);
		
		if(value==null){
			SysProperty pro=findByProFromDBA(key);
			if(pro!=null){
				value=pro.getProValue();
			}
		}
		return value;
	}
	
	public SysProperty findByProFromDBA(String key){
		return sysPropertyDAO.findByProKey(key);
	}

	@Override
	public void save(SysProperty t) {
		// TODO Auto-generated method stub
		sysPropertyDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		sysPropertyDAO.delete(id);
	}

	@Override
	public SysProperty findById(Long id) {
		// TODO Auto-generated method stub
		return sysPropertyDAO.findOne(id);
	}

	@Override
	public void delete(SysProperty t) {
		// TODO Auto-generated method stub
		sysPropertyDAO.delete(t);
		
	}
	
	
	

}
