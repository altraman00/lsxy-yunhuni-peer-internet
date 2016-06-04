package com.lsxy.framework.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

@SuppressWarnings("rawtypes")
public interface BaseService <T>{
	


	public T findById(String id);
	
	public long count();
	
	public T save(T entity);
	
	public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException;
	
	public void delete(T entity) throws IllegalAccessException, InvocationTargetException;
	
	public List<T> list(); 
	

	public Page pageList(int pageNo, int pageSize);
	
	public List<T> list(String hql, Object... objects);
	
	public Page pageList(String hql, int pageNo, int pageSize, Object... params);
	
	public int execHql(String hql, Object... objects);
	
	

	public T findUnique(String hql, Object... params) throws MatchMutiEntitiesException;
	
	
	public void batchInsert(List entitis);
	
	public void batchDelete(List entities);
	
}
