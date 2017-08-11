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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmp.res.dao.UserDAO;
import com.cmp.res.entity.User;
import com.cmp.res.util.CommonUtil;


@Service("userService")
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PasswordHelper passwordHelper;
	@Autowired
	private CommonService commonService;
	
	public User getUserByName(String userName){
		return userDAO.findByUserName(userName);
		
	}
	
	public User findUserById(Long id){
		return userDAO.findOne(id);
	}
	
	
	
	public boolean add(User user){
		boolean flag=true;
		//加密密码
		if(user.getId()==null)
		passwordHelper.encryptPassword(user);
		try{
			userDAO.save(user);
		}catch(Exception e){
			e.printStackTrace();
			flag=false;
		}
		
		return flag;
	}
	
	public Page<User> searchUser(Integer pn,Integer ps){
		User user=commonService.getCurrentLogin();
		
		PageRequest pageRequest = CommonUtil.buildPageRequest(pn, ps, null, null, null);
		return userDAO.findAll(getSpec(user.getUserLevel()), pageRequest);
		
	}
	
	public Specification<User> getSpec(final Integer userLevel) {
		return new Specification<User>() {
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				Path<Integer> userLevelPath = root.get("userLevel");
				predicates.add(cb.greaterThan(userLevelPath,userLevel));
			

				Predicate[] arr = predicates.toArray(new Predicate[predicates.size()]);

				return query.where(arr).getRestriction();
			}
		};
	}

}
