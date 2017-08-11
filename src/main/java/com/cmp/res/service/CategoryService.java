package com.cmp.res.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmp.res.dao.BookDAO;
import com.cmp.res.dao.CategoryDAO;
import com.cmp.res.dao.ItemDAO;
import com.cmp.res.dao.UserDAO;
import com.cmp.res.entity.Book;
import com.cmp.res.entity.Category;
import com.cmp.res.entity.Item;
import com.cmp.res.entity.User;


@Service
@Transactional(readOnly = true)
public class CategoryService {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	public List<Category> findByParentid(Long pid,Integer resourcesType){
//		return categoryDAO.findByParentidAndResourcesType(pid,resourcesType);
		return categoryDAO.findAll(getSpec(pid,resourcesType));
	}
	
	public Category findById(Long id){
		//System.out.println("aaaa" + categoryDAO.findByName("机械制造工艺分类"));
		return categoryDAO.findOne(id);
	}
	
	
	@Transactional(readOnly = false)
	public void saveCategory(Category category){
		
		categoryDAO.save(category);
		
	}
	@Transactional(readOnly = false)
	public void delete(Category category){
		categoryDAO.delete(category);
	}
	
	
	public Category findByParentidAndNameResourcesType(Long pid,String name,Integer resourcesType){
		List<Category> list= categoryDAO.findByParentidAndNameAndResourcesType(pid, name,resourcesType);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	
	public Specification<Category> getSpec(final Long pid,final Integer resourcesType) {
		return new Specification<Category>() {
			public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Integer> resourcesTypePath = root.get("resourcesType");
				switch (resourcesType) {
				case 1:
					predicates.add(cb.or(cb.equal(resourcesTypePath, 0), cb.equal(resourcesTypePath, 1)));
					break;
				case 2:
					predicates.add(cb.or(cb.equal(resourcesTypePath, 0), cb.equal(resourcesTypePath, 2)));
					break;
				default:
					predicates.add(cb.equal(resourcesTypePath, 0));
				}
				if(pid!=null){
					Path<Long> parentPath = root.get("parent");
					predicates.add(cb.equal(parentPath, pid));
				}
				

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}
	
	
	

}
