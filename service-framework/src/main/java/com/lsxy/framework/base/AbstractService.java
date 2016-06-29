package com.lsxy.framework.base;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.HqlUtil;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.api.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public abstract class AbstractService<T> implements BaseService<T> {

	private Logger logger = LoggerFactory.getLogger(AbstractService.class);

	public abstract BaseDaoInterface<T,Serializable> getDao();

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public T findById(String id) {
		return getDao().findOne(id);
	}

	@Override
	public long count() {
		return getDao().count();
	}

	@Override
	public T save(T entity) {
		return getDao().save(entity);
	}

	@Override
	public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException {
		this.logicDelete(id);
	}

	@Override
	public void delete(T entity) throws IllegalAccessException, InvocationTargetException {
		this.logicDelete(entity);
	}

	@Override
	public Iterable<T> list() {
		return this.getDao().findAll();
	}

	@Override
	public Page<T> pageList(int pageNo, int pageSize) {
		String hql = "from "+this.getRealClass().getName() + " obj";
		return pageList(hql,pageNo,pageSize);
//		return null;
	}

	/**
	 * 根据id逻辑删除实体对象
	 * @param id
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void logicDelete(Serializable id) throws IllegalAccessException, InvocationTargetException{
		T obj = this.getDao().findOne(id);
		this.logicDelete(obj);
	}
	
	/**
	 * 逻辑删除实体对象,将对象属性delete 设置为true
	 * @param obj
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void logicDelete(T obj) throws IllegalAccessException, InvocationTargetException{
		if(obj != null){
			BeanUtils.setProperty(obj, "deleted", true);
			this.save(obj);
			this.getEm().flush();
//			this.getEm().detach(obj);
		}
	}


	/**
	 * 根据条件执行逻辑删除
	 * @param property
	 * @param value
	 */
	public void logicDeleteByCondition(String property,Object value){
		Class<T> x = this.getRealClass();
		String hql = "update "+x.getName()+" as obj set obj.deleted=true";
		hql = HqlUtil.addCondition(hql, property,value,HqlUtil.LOGIC_AND,HqlUtil.TYPE_OBJECT);
		Query query = this.getEm().createQuery(hql);
		query.setParameter(property.replace(".", ""), value);
		int count = query.executeUpdate();
		logger.debug("hql:"+hql);
		logger.debug("result:"+count);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getRealClass() {
		Type genType = this.getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		return (Class<T>) params[0];
	}

	@Override
	public List list(String hql, Object... params) {
		Query query = this.getEm().createQuery(hql);
		for (int i = 0; i < params.length; i++) {
			Object object = params[i];
			query.setParameter(i+1, object);
		}
		return query.getResultList();
	}

	@Override
	public Page<T> pageList(String hql, int pageNo, int pageSize, Object... params) {
		return this.findByCustom(hql,true, pageNo, pageSize,params);
	}

	@Override
	public T findUnique(String hql, Object ... params)
			throws MatchMutiEntitiesException {
		List<T> results = findByCustomWithParams(hql, params);
		if(results.size() == 0)
			return null;
		if(results.size()>1)
			throw new MatchMutiEntitiesException();
		else{
			return results.get(0);
		}
	}



	/**
	 * 自定义查询方法，不带分页,默认排除掉deleted数据
	 * @param jpql
	 * @param params
	 * @return
	 */
	public List findByCustomWithParams(String jpql,Object ... params){
		return this.findByCustomWithParams(jpql,true, params);
	}

	/**
	 * from LoginLog ll where ll.personId='' order by ll.dt desc
	 */
	public List findByCustomWithParams(String jpql,boolean excludeDeleted,Object ... params){
		if(excludeDeleted)
			jpql = HqlUtil.addCondition(jpql, "deleted", 0,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
		Query query = this.em.createQuery(jpql);
		for (int i = 0; i < params.length; i++) {
			Object object = params[i];
			query.setParameter(i+1, object);
		}
		List loglist = query.getResultList();
		return loglist;
	}
	/**
	 * 自定义查询方法，带分页
	 * 默认排除deleted
	 */
	public Page findByCustom(String jpql,int pageNo,int pageSize){
		return this.findByCustom(jpql,true, pageNo, pageSize);
	}

	/**
	 * from LoginLog ll where ll.personId='' order by ll.dt desc
	 */
	public Page<T> findByCustom(String jpql,boolean excludeDeleted,int pageNo,int pageSize,Object ... params){
		logger.debug("findByCustom:"+jpql);
		if(excludeDeleted)
			jpql = HqlUtil.addCondition(jpql, "deleted", 0,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
		pageNo--;
		String countJpql = " select count (*) " + HqlUtil.removeOrders(HqlUtil.removeSelect(jpql));
		Query query = this.em.createQuery(countJpql);
		for (int i = 0; i < params.length; i++) {
			Object object = params[i];
			query.setParameter(i+1, object);
		}
		Object obj = query.getSingleResult();
		long totalCount = (Long) obj;

		query = this.em.createQuery(jpql);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageNo*pageSize);
		List list = query.getResultList();
		Page page = new Page((pageNo)*pageSize+1,totalCount,pageSize,list);
		return page;
	}

}
