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

import com.cmp.res.dao.VideoDAO;
import com.cmp.res.entity.Photo;
import com.cmp.res.entity.Video;
import com.cmp.res.util.CommonUtil;

@Service
public class VideoService implements BaseService<Video>{
	@Autowired
	private VideoDAO videoDAO;
	
	public Page<Video> outline(int pn,int ps,Long catid){
		return videoDAO.findAll(getSpec(catid), CommonUtil.buildPageRequest(pn, ps, null, null, null));
	}
	public Long getCount(){
		return videoDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	
	public List<Video>  findByIdIn(Long[] ids){
		return videoDAO.findByIdIn(ids);
	}
	
//	public List<Video> getVideo(){
//		return videoDAO.findByPicIsNull();
//	}

	@Override
	public void save(Video t) {
		// TODO Auto-generated method stub
		videoDAO.save(t);
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		videoDAO.delete(id);
	}

	@Override
	public Video findById(Long id) {
		// TODO Auto-generated method stub
		return videoDAO.findOne(id);
	}

	@Override
	public void delete(Video t) {
		// TODO Auto-generated method stub
		videoDAO.delete(t);
		
	}
	
	public Specification<Video> getSpec(final Long catid) {
		return new Specification<Video>() {
			public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(catid!=null){
					Path<Long> catidPath = root.get("catid");
					predicates.add(cb.equal(catidPath, catid));
				}
				
				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
