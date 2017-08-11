package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.SubjectItemDAO;
import com.cmp.res.entity.SubjectItem;
import com.cmp.res.util.CommonUtil;
@Service
public class SubjectItemService implements BaseService<SubjectItem>{
	@Autowired
	private SubjectItemDAO subjectItemDAO;
	
	public List<SubjectItem> findByItemidAndType(long itemid,int type){
		return subjectItemDAO.findByItemidAndType(itemid,type);
	}
	
	public Page<SubjectItem> findBySubjectid(Long subjectid,Integer pn,Integer ps){
		return subjectItemDAO.findAll(getSpec(subjectid), CommonUtil.buildPageRequest(pn, ps, null,null,null));
	}
	
	public SubjectItem findBySubjectidAndItemid(Long subjectid,Long itemid){
		List<SubjectItem>  sis=subjectItemDAO.findBySubjectidAndItemid(subjectid,itemid);
		return sis!=null&&sis.size()==1?sis.get(0):null;
	}

	@Override
	public void save(SubjectItem subjectItem) {
		// TODO Auto-generated method stub
		subjectItemDAO.save(subjectItem);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		subjectItemDAO.delete(id);
		
	}
	
	public void deleteBySubjectid(Long id){
		subjectItemDAO.deleteBySubjectid(id);
	}

	@Override
	public SubjectItem findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void delete(SubjectItem t) {
		// TODO Auto-generated method stub
		
	}
	
	public Specification<SubjectItem> getSpec(final Long subjectid) {
		return new Specification<SubjectItem>() {
			public Predicate toPredicate(Root<SubjectItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(subjectid!=null){
					Path<Long> subjectidPath = root.get("subjectid");
					predicates.add(cb.equal(subjectidPath, subjectid));
				}
				
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
}
