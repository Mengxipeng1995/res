package com.cmp.res.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort.Order;
import com.cmp.res.dao.ResourceDAO;
import com.cmp.res.entity.Resource;
@Service
public class ResourceService implements BaseService<Resource> {
	@Autowired
	private ResourceDAO resourceDAO;
	
	public Iterator<Resource> findAll(){
//		Order order = new Order(Direction.DESC, "type");
//		List<Resource> list=new ArrayList<Resource>();
		Iterator<Resource> it=resourceDAO.findAll().iterator();
//		while(it.hasNext()){
//			list.add(it.next());
//		}
//		return list;
		return it;
	
	}

	@Override
	public void save(Resource t) {
		// TODO Auto-generated method stub
		resourceDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		resourceDAO.delete(id);
	}

	@Override
	public Resource findById(Long id) {
		// TODO Auto-generated method stub
		return resourceDAO.findOne(id);
	}

	@Override
	public void delete(Resource t) {
		// TODO Auto-generated method stub
		resourceDAO.delete(t);
		
	}

}
