package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.BibliographyDAO;
import com.cmp.res.dao.IndexentryDAO;
import com.cmp.res.dao.ItemDAO;
import com.cmp.res.entity.Bibliography;
import com.cmp.res.entity.Indexentry;
import com.cmp.res.entity.Item;
import com.cmp.res.util.CommonUtil;


@Service
@Transactional(readOnly = true)
public class BibliographyService {
	
	@Autowired
	private BibliographyDAO bibliographyDAO;
	
	
	
	@Transactional(readOnly = false)
	public void saveBibliography(Bibliography biblio){
		
		bibliographyDAO.save(biblio);
		
	}
	
	
	

}
