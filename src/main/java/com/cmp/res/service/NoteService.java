package com.cmp.res.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.NoteDAO;
import com.cmp.res.entity.Note;

@Service
public class NoteService implements BaseService<Note>{
	@Autowired
	private NoteDAO noteDAO;

	@Override
	public void save(Note t) {
		noteDAO.save(t);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		noteDAO.delete(id);
		// TODO Auto-generated method stub
		
	}

	@Override
	public Note findById(Long id) {
		// TODO Auto-generated method stub
		return noteDAO.findOne(id);
	}

	@Override
	public void delete(Note t) {
		// TODO Auto-generated method stub
		noteDAO.delete(t);
	}

}
