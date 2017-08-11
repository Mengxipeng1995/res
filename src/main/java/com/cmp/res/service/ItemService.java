package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.ItemDAO;
import com.cmp.res.entity.Item;
import com.cmp.res.util.CommonUtil;


@Service
@Transactional(readOnly = true)
public class ItemService {
	
	@Autowired
	private ItemDAO itemDAO;
	
	public Long getCount(){
		return itemDAO.findAll(CommonUtil.buildPageRequest(1, 1, null, null, null)).getTotalElements();
	}
	
	
	public Item findById(Long id){
		return itemDAO.findOne(id);
	}
	
	public List<Item> findByResCode(String resCode){
		return itemDAO.findByResCode(resCode);
	}
	
	public List<Item> findByCatid(Long id){
		return itemDAO.findByCatid(id);
	}
	public List<Item> findSect(Long bookid,String linked){
		return itemDAO.findSect(bookid, linked);
	}
	
	public List<Item> findByIdIn(Long[] ids){
		return itemDAO.findByIdIn(ids);
	}
	
	public List<Item> findChaper(Long bookid){
		return itemDAO.findChapter(bookid);
	}
	
	@Transactional(readOnly = false)
	public void saveItem(Item item){
		
		itemDAO.save(item);
		
	}
	
	public Page<Item> listItemByResCode(String resCode,Integer pn,Integer ps){
		List< Order> orders=new ArrayList< Order>();
		orders.add( new Order(Direction.DESC, "createDate"));
		
		return itemDAO.findAll(getSpecResCode(resCode),CommonUtil.buildPageRequest(pn, ps, null, null, new Sort(orders)));
	}
	
	
	public Page<Item> outline(int pn,int ps,Long catid){
		PageRequest pageRequest = CommonUtil.buildPageRequest(pn, ps, null, null, null);
		//获取分页相关信息
		//Page<Item> page=itemDAO.findAll(getSpec(catid), pageRequest);
		//按照相同资源的最新版本
		//page.getContent().clear();
		//page.getContent().addAll(itemDAO.outline(catid, (pn-1)*ps, ps));
		
		return itemDAO.outline(catid, pageRequest);
	}
	
	
	public Specification<Item> getSpecResCode(final String resCode) {
		return new Specification<Item>() {
			public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(resCode)){
					Path<String> resCodePath = root.get("resCode");
					predicates.add(cb.equal(resCodePath, resCode));
				}
				
				Path<Integer> deleteFlagPath = root.get("deleteFlag");
				predicates.add(cb.notEqual(deleteFlagPath, 1));
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
	public Specification<Item> getSpec(final Long catid) {
		return new Specification<Item>() {
			public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(catid!=null){
					Path<Long> catidPath = root.get("catid");
					predicates.add(cb.equal(catidPath, catid));
				}
				/**
				 *默认展示  原始版本的条目
				 */
				Path<Integer> originalVersionFlagPath=root.get("originalVersionFlag");
				predicates.add(cb.or(cb.equal(originalVersionFlagPath, 0),cb.equal(originalVersionFlagPath, 1)));
			
				Path<Integer> deleteFlagPath = root.get("deleteFlag");
				predicates.add(cb.notEqual(deleteFlagPath, 1));
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	

}
