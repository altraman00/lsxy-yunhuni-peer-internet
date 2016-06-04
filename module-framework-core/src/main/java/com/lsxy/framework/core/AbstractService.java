package com.lsxy.framework.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Query;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

@SuppressWarnings({"unchecked","rawtypes"})
public abstract class AbstractService<T> implements BaseService<T>{
	
	public abstract AbstractManager<T> getManager();

	@Override
	public T findById(String id) {
		return getManager().findById(id);
	}

	@Override
	public long count() {
		return getManager().count();
	}

	@Override
	public T save(T entity) {
		return getManager().save(entity);
	}

	@Override
	public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException {
		getManager().logicDelete(id);
	}
	
	@Override
	public List<T> list() {
		String className = getManager().getEntityClass().getClass().getName();
		String hql = " from "+className+" obj where obj.deleted=false";
		return list(hql);
	}
	
	@Override
	public List list(String hql, Object... params) {
		Query query = getManager().getEm().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			Object object = params[i];
			query.setParameter(i+1, object);
		}
		return query.getResultList();
	}

	@Override
	public Page pageList(String hql, int pageNo, int pageSize, Object... params) {
		return getManager().findByCustom(hql,true, pageNo, pageSize,params);
	}

	@Override
	public int execHql(String hql, Object... params) {
		return getManager().exec(hql,params);
	}

	@Override
	public T findUnique(String hql, Object ... params)
			throws MatchMutiEntitiesException {
		List<T> results = getManager().findByCustomWithParams(hql, params);
		if(results.size() == 0)
			return null;
		if(results.size()>1)
			throw new MatchMutiEntitiesException();
		else{
			return results.get(0);
		}
	}

	@Override
	public void delete(T entity) throws IllegalAccessException, InvocationTargetException {
		this.getManager().logicDelete(entity);
	}

	@Override
	public void batchInsert(List entitis) {
		this.getManager().batchInsert(entitis);
		
	}

	@Override
	public void batchDelete(List entitis) {
		// TODO Auto-generated method stub
		this.getManager().batchDelete(entitis);
	}

	@Override
	public Page pageList(int pageNo, int pageSize) {
		String className = getManager().getEntityClass().getName();
		String hql = " from "+className+" obj where obj.deleted=false";
		Page page =  this.pageList(hql, pageNo, pageSize);
		return page;
	}



}
