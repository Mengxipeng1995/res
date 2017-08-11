package com.cmp.res.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.GroupDAO;
import com.cmp.res.entity.ChapterCategory;
import com.cmp.res.entity.Group;

@Service
public class GroupService {
	@Autowired
	private GroupDAO groupDAO;
	
	public Group getRoot(){
		return groupDAO.findByParentidIsNull();
	}
	
	public void save(Group group){
		groupDAO.save(group);
	}
	
	public Group findById(Long id){
		return groupDAO.findOne(id);
	}
	
	public List<Group> findByParentid(Long id){
		return groupDAO.findByParentid(id);
	}

	public void delete(Group group){
		groupDAO.delete(group);
	}
	
	public Group findByParentidAndName(Long id,String name){
		List<Group> group=groupDAO.findByParentidAndName(id,name);
		if(group!=null&&group.size()>0){
			return group.get(0);
		}
		return null;
		
	}
}
