package com.lsxy.framework.api.base;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("rawtypes")
public interface BaseService<T>{

	public T findById(String id);
	
	public long count();
	
	public T save(T entity);
	
	public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException;
	
	public void delete(T entity) throws IllegalAccessException, InvocationTargetException;
	
	public Iterable<T> list();

	public Page<T> pageList(int pageNo, int pageSize);
	
	public Iterable<T> list(String hql, Object... objects);

	public Page pageList(String hql, int pageNo, int pageSize, Object... params);

	public T findUnique(String hql, Object... params) throws MatchMutiEntitiesException;

	public Iterable<T> save(Iterable<T> list);
	
}
