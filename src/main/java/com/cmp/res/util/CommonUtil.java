package com.cmp.res.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class CommonUtil {
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType, Direction direction, Sort sort) {
		if (sort == null) {
			if (StringUtils.isBlank(sortType)) {
				return new PageRequest(pageNumber - 1, pagzSize);
			}
			Sort sort1 = new Sort(direction, sortType);
			return new PageRequest(pageNumber - 1, pagzSize, sort1);
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	public static List<Long> getCids(String idsStr){
		if(StringUtils.isNotBlank(idsStr)&&idsStr.split(";").length>0){
			List<Long> list=new ArrayList<Long>();
			for(String id:idsStr.split(";")){
				try{
					list.add(Long.parseLong(id));
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			return list;
		}else{
			return null;
		}
	}
	
	
	

}
