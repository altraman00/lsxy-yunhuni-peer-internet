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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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
    public Page<VoiceFilePlay> pageList(Integer pageNo, Integer pageSize, String name,String appId,String[] tenantId,Integer status,String startTime,String endTime) {
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
            sql += " AND obj.createTime>= :date1 ";
        }
        if(date2!=null){
            sql += " AND obj.createTime<= :date2";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" GROUP BY obj.create_time DESC";
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
    @Cacheable(value="entity",key="'entity_'+#appId+'_'+#name",unless = "#result == null")
    public String getVerifiedFile(String appId, String name) {
        String hql = " from VoiceFilePlay obj  where obj.appId = ?1 and obj.status = ?2 and obj.name= ?3";
        VoiceFilePlay file = null;
        try {
            file = this.findUnique(hql,appId,name,1);
        } catch (MatchMutiEntitiesException e) {
            logger.error("app放音文件重复",e);
        }
        if(file == null){
            return null;
        }
        return file.getId();
    }

    @Override
    public List<VoiceFilePlay> findNotSync() {
        String hql = "from VoiceFilePlay obj where ( obj.sync<>?1 or obj.sync is null )and obj.status=?2 and obj.app.deleted='0' group by obj.lastTime ";
        List<VoiceFilePlay> list = this.list(hql,VoiceFilePlay.SYNC_SUCCESS,VoiceFilePlay.STATUS_SUCCESS);
        return list;
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
            String sql = "update db_lsxy_bi_yunhuni.tb_bi_voice_file_play set sync=? where  deleted=0 and id in( " + id + " )";
            jdbcTemplate.update(sql, sync);
        }
    }
}
