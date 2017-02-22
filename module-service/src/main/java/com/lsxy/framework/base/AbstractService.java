package com.lsxy.framework.base;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.exceptions.PageSizeTooLargeException;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.HqlUtil;
import com.lsxy.framework.core.utils.Page;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings({"unchecked","rawtypes"})
@Transactional
public abstract class AbstractService<T extends IdEntity> implements BaseService<T> {

    private static Logger logger = LoggerFactory.getLogger(AbstractService.class);

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
    @Cacheable(value="entity",key="'entity_'+#id",unless = "#result == null")
    public T findById(String id) {
        return getDao().findOne(id);
    }

    @Override
    public long count() {
        return getDao().count();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true)
            }
    )
    public T save(T entity) {
        //更新最后修改时间
        entity.setLastTime(new Date());
        return getDao().save(entity);
    }

    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException {
        this.logicDelete(id);
    }

    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true)
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
//      return null;
    }

    /**
     * 根据id逻辑删除实体对象
     * @param id
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
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
    @CacheEvict(value = "entity", key = "'entity_' + #obj.id", beforeInvocation = true)
    public void logicDelete(T obj) throws IllegalAccessException, InvocationTargetException{
        if(obj != null){
            //设置删除时间
            obj.setDeleteTime(new Date());
            obj.setDeleted(true);
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
        if(logger.isDebugEnabled()){
            logger.debug("hql:"+hql);
            logger.debug("result:"+count);
        }
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
    public Page pageList(String hql, int pageNo, int pageSize, Object... params) {
        if(pageSize > 1000){
            throw new PageSizeTooLargeException("分页数据太大");
        }
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
    public Page findByCustom(String jpql,boolean excludeDeleted,int pageNo,int pageSize,Object ... params){

        long totalCount = countByCustom(jpql, excludeDeleted,params);
        pageNo--;
        List list = getPageList(jpql,excludeDeleted, pageNo, pageSize, params);
        Page page = new Page((pageNo)*pageSize+1,totalCount,pageSize,list);
        return page;
    }

    /**
     * 获取分页里面的数据,排除删除的数据
     * @return
     */
    public List getPageList(String jpql,int pageNo, int pageSize, Object... params) {
        return getPageList(jpql,true,pageNo,pageSize,params);
    }

    /**
     * 获取分页里面的数据 注：传进来的 pageNo 要先减去 1
     * @return
     */
    public List getPageList(String jpql, boolean excludeDeleted,int pageNo, int pageSize, Object... params) {
        if(logger.isDebugEnabled()){
            logger.debug("findPageList:"+jpql);
        }
        if(excludeDeleted){
            jpql = HqlUtil.addCondition(jpql, "deleted", 0,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
        }
        Query query = this.em.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            Object object = params[i];
            query.setParameter(i+1, object);
        }
        query.setMaxResults(pageSize);
        query.setFirstResult(pageNo*pageSize);
        return query.getResultList();
    }

    public List getTopList(String jpql,boolean excludeDeleted, int top, Object... params) {
        if(logger.isDebugEnabled()){
            logger.debug("getTopList:"+jpql);
        }
        if(excludeDeleted){
            jpql = HqlUtil.addCondition(jpql, "deleted", 0,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
        }
        Query query = this.em.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            Object object = params[i];
            query.setParameter(i+1, object);
        }
        query.setMaxResults(top);
        query.setFirstResult(0);
        return query.getResultList();
    }

    /**
     * 根据条件查询总数
     * @param jpql
     * @param params 参数
     * @return
     */
    public long countByCustom(String jpql,Object ... params){
        return this.countByCustom(jpql,true,params);
    }

    /**
     * 根据条件查询总数
     * @param jpql
     * @param excludeDeleted 是否排除已删除的
     * @param params 参数
     * @return
     */
    public long countByCustom(String jpql,boolean excludeDeleted,Object ... params){
        if(logger.isDebugEnabled()){
            logger.debug("countByCustom:"+jpql);
        }
        if(excludeDeleted){
            jpql = HqlUtil.addCondition(jpql, "deleted", 0,HqlUtil.LOGIC_AND,HqlUtil.TYPE_NUMBER);
        }
        jpql = jpql.replaceAll("fetch", "");
        String countJpql = " select count(1) " + HqlUtil.removeOrders(HqlUtil.removeSelect(jpql));
        Query query = this.em.createQuery(countJpql);
        for (int i = 0; i < params.length; i++) {
            Object object = params[i];
            query.setParameter(i+1, object);
        }
        Object obj = query.getSingleResult();
        long totalCount = (Long) obj;
        return totalCount;
    }

    public Iterable<T> save(Iterable<T> list){
        return getDao().save(list);
    }

    /***
     * 更新实体类，只更新不为null的属性，用于代替save更新实体类，save是全部更新，并发下会导致更新被覆盖
     * PS:只能修改当前类的属性,不能修改父类(IdEntity)的属性
     * @param id
     * @param obj
     */
    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void update(String id,T obj){
        if(id == null || obj == null){
            return;
        }
        boolean update = false;
        String className = obj.getClass().getSimpleName();
        StringBuilder hql = new StringBuilder("update "+className+" a set");
        Set<Map.Entry<String,Object>> fields = attrValues(obj).entrySet();
        List<String> colums = new ArrayList<>();
        for (Map.Entry<String,Object> field: fields) {
            colums.add(" a."+field.getKey()+"=:"+field.getKey());
            update = true;
        }
        if(update){
            hql.append(StringUtils.join(colums,","));
            Query query = this.em.createQuery(hql.append(" where a.id=:id").toString());
            query.setParameter("id",id);
            for (Map.Entry<String,Object> field: fields) {
                query.setParameter(field.getKey(),field.getValue());
            }
            query.executeUpdate();
        }
    }

    private Map<String,Object> attrValues(T obj){
        Map<String,Object> values = new HashedMap();
        Set<String> fields = getFields(obj.getClass());
        for (String field: fields) {
            Object attrValue = getAttrValue(obj,field);
            if(attrValue!=null){
                values.put(field,attrValue);
            }
        }
        return values;
    }
    private Object getAttrValue(T obj, String attr) {
        try {
            Field privateField = obj.getClass().
                    getDeclaredField(attr);

            privateField.setAccessible(true);

            Object fieldValue = privateField.get(obj);
            return fieldValue;
        } catch (Exception e) {
            logger.info("反射获取属性值失败obj={},{}",obj,e);
            throw new IllegalArgumentException(e);
        }
    }

    private static Set<String> getFields(Class c){
        Set<String> fields = new HashSet<>();
        try {
            Method[] methods = c.getDeclaredMethods();
            Field[] attrs = c.getDeclaredFields();
            for (Method method : methods) {
                //判断方法上是否存在Column这个注解
                if (method.isAnnotationPresent(Column.class)) {
                    String field = method.getName();
                    field = field.replace("get","").replace("set","");
                    field= field.substring(0,1).toLowerCase()
                            .concat(field.substring(1));
                    fields.add(field);
                }
            }
            for (Field field: attrs) {
                //判断方法上是否存在Column这个注解
                if (field.isAnnotationPresent(Column.class)) {
                    fields.add(field.getName());
                }
            }
        } catch (Throwable e) {
            logger.info("反射获取实体类属性失败class={},{}",c,e);
            throw new IllegalArgumentException(e);
        }
        return fields;
    }

    public Iterable<T> findAll(Collection<String> ids){
        if(ids == null || ids.size() == 0){
            return null;
        }
        return this.getDao().findAll(new ArrayList<Serializable>(ids));
    }
    /*public static void main(String[] args) {
        System.out.println(getFields(Tenant.class));
    }*/
}
