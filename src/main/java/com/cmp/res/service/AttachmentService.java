package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.AttachmentDAO;
import com.cmp.res.entity.Attachment;

import java.util.List;


@Service
public class AttachmentService implements BaseService<Attachment>{
	
	@Autowired
	private AttachmentDAO attachmentDAO;

	@Override
	public void save(Attachment t) {
		// TODO Auto-generated method stub
		attachmentDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		attachmentDAO.delete(id);
	}

	@Override
	public Attachment findById(Long id) {
		// TODO Auto-generated method stub
		return attachmentDAO.findOne(id);
	}

	@Override
	public void delete(Attachment t) {
		// TODO Auto-generated method stub
		attachmentDAO.delete(t);
	}

//	public Attachment findByRecouseId(Long bookid) {
//		// TODO Auto-generated method stub
//		 return  attachmentDAO.findByResouceId(bookid);
//	}


	public Iterable<Attachment> findAllByResouceId(Long bookid){
		return attachmentDAO.findAllByResouceId(bookid);
	}

}
