package com.cmp.res.service;

public interface BaseService<T> {
	
	public  void save(T t);
	
	public void delete(Long id);
	
	public T findById(Long id);
	
	public void delete(T t);

}
