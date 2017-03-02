package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.file.dao.VoiceFilePlayDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@Service
public class VoiceFilePlayServiceImpl extends AbstractService<VoiceFilePlay> implements VoiceFilePlayService{

    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VoiceFilePlayDao voiceFilePlayDao;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<VoiceFilePlay, Serializable> getDao() {
        return voiceFilePlayDao;
    }

    @Override
    public VoiceFilePlay findById(String id) {
        return getDao().findOne(id);
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true),
                    @CacheEvict(value="playfile",key="'playfile_'+#entity.app.id+'_'+#entity.name", beforeInvocation = true)
            }
    )
    public void delete(VoiceFilePlay entity) throws IllegalAccessException, InvocationTargetException {
        super.delete(entity);
    }

    @Override
    public Page<VoiceFilePlay> pageList(Integer pageNo, Integer pageSize, String name,String appId,String[] tenantId,Integer status,String startTime,String endTime,String subId) {
        String sql = " FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_play obj WHERE obj.deleted=0 ";
        if(StringUtils.isNotEmpty(name)){
            sql += " AND obj.name like '%"+name+"%' ";
        }
        if(StringUtil.isNotEmpty(appId)){
            sql +=" AND obj.app_id= '"+appId+"' ";
        }
        if(tenantId!=null&& tenantId.length>0){
            String tenantIds = "";
            for(int i=0;i<tenantId.length;i++){
                tenantIds += " '"+tenantId[i]+"' ";
                if(i!=(tenantId.length-1)){
                    tenantIds+=",";
                }
            }
            sql +=" AND obj.tenant_id in("+tenantIds+") ";
        }
        if(status!=null){
            sql += " AND obj.status='"+status+"' ";
        }
        if(StringUtils.isNotEmpty(subId)){
            sql += " AND obj.subaccount_id='"+subId+"'";
        }
        Date date1 = null;
        if(StringUtil.isNotEmpty(startTime)){
            try{
                date1 = DateUtils.parseDate(startTime,"yyyy-MM-dd");
            }catch (Exception e){}
        }
        Date date2 = null;
        if(StringUtil.isNotEmpty(endTime)){
            try{
                date2 = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
            }catch (Exception e){}
        }
        if(date1!=null){
            sql += " AND obj.create_time>= :date1 ";
        }
        if(date2!=null){
            sql += " AND obj.create_time<= :date2";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" ORDER BY obj.create_time DESC";
        Query pageQuery = em.createNativeQuery(pageSql,VoiceFilePlay.class);
        if(date1!=null){
            countQuery.setParameter("date1",date1);
            pageQuery.setParameter("date1",date1);
        }
        if(date2!=null){
            countQuery.setParameter("date2",date2);
            pageQuery.setParameter("date2",date2);
        }
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }

    @Override
    @Cacheable(value="playfile",key="'playfile_'+#appId+'_'+#name",unless = "#result == null")
    public String getVerifiedFile(String appId, String name) {
        String hql = "from VoiceFilePlay obj where obj.app.id = ?1 and obj.status = ?2 and obj.name= ?3 and obj.sync = ?4";
        VoiceFilePlay file = null;
        try {
            file = this.findUnique(hql,appId,1,name,1);
        } catch (MatchMutiEntitiesException e) {
            logger.error("app放音文件重复",e);
        }
        if(file == null){
            return null;
        }
        return file.getId();
    }

    @Override
    public List<String> findNotSyncApp() {
        String sql = "SELECT app_id AS appId FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_play obj where ( obj.sync<>? or obj.sync is null )and obj.status=?  group by obj.app_id ";
        List<String> list = jdbcTemplate.queryForList(sql,String.class,VoiceFilePlay.SYNC_SUCCESS,VoiceFilePlay.STATUS_SUCCESS);
        return list;
    }

    @Override
    public List<VoiceFilePlay> findNotSyncByApp(String app) {
        String hql = "from VoiceFilePlay obj where ( obj.sync<>?1 or obj.sync is null )and obj.status=?2 and obj.app.id=?3 ";
        List<VoiceFilePlay> list = this.list(hql,VoiceFilePlay.SYNC_SUCCESS,VoiceFilePlay.STATUS_SUCCESS,app);
        return list;
    }

    @Override
    public List<VoiceFilePlay> findByAppId(String appId) {
        return voiceFilePlayDao.findByAppId(appId);
    }


    @Override
    public void batchUpdateSync(List<String> ids, Integer sync) {
        if(ids.size()>0) {
            String id = "";
            for (int i = 0; i < ids.size(); i++) {
                id += " '" + ids.get(i) + "' ";
                if (i != (ids.size() - 1)) {
                    id += ",";
                }
            }
            String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play SET sync=? WHERE  deleted=0 AND id IN( " + id + " )";
            jdbcTemplate.update(sql, sync);
        }
    }

    @Override
    public void batchUpdateValueByKey(List<String> ids, String key, Object value) {
        if(ids.size()>0) {
            String id = "";
            for (int i = 0; i < ids.size(); i++) {
                id += " '" + ids.get(i) + "' ";
                if (i != (ids.size() - 1)) {
                    id += ",";
                }
            }
            String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play SET "+key+"=? WHERE id IN( " + id + " )";
            jdbcTemplate.update(sql, value);
        }
    }

    @Override
    public void updateDeletedByAppId(String appId) {
        String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play SET deleted=1,oss_deleted=-1,aa_deleted=-1 WHERE app_id=? ";
        jdbcTemplate.update(sql, appId);
    }

    @Override
    public void updateDeletedStautsByAppId(String appId, Object status) {
        String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play SET aa_deleted=? WHERE app_id=? ";
        jdbcTemplate.update(sql,status, appId);
    }

    @Override
    public void updateDeletedStautsByid(String id, Object status) {
        String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play SET aa_deleted=? WHERE id=? ";
        jdbcTemplate.update(sql, status, id);
    }
    @Override
    public List<VoiceFilePlay> findByFileName(String tenantId, String appId, String name) {
        return this.list("from VoiceFilePlay obj where obj.tenant.id = ?1 and obj.app.id=?2 and name = ?3 ",tenantId,appId,name);
    }

    @Override
    public void renewSyncByAppId(String appId) {
        String sql = "UPDATE db_lsxy_bi_yunhuni.tb_bi_voice_file_play a SET a.sync=? WHERE a.app_id=? and a.deleted=0";
        jdbcTemplate.update(sql, 0, appId);
    }
    @Override
    public List<Map> getOssListByDeleted() {
        String sql = " SELECT id as id ,file_key as ossUrl FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_play WHERE deleted=1 and status=1 and file_key is not null and file_key<>'' and (oss_deleted is null or oss_deleted<>1)";
        List<Map> list = jdbcTemplate.queryForList(sql,Map.class);
        return list;
    }
}
