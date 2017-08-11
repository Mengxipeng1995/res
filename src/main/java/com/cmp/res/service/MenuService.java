package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.MenuDAO;
import com.cmp.res.entity.Menu;



@Service
public class MenuService {
	
	@Autowired
	private MenuDAO menuDAO;
	
	/**
	 * 根据父节点id获取子节点
	 */
	public List<Menu> getMenuByParentId(Long parentId){
		
		return menuDAO.findByParentid(parentId);
		
	}
	
	
	public Menu findById(Long id){
		return menuDAO.findOne(id);
	}

}
