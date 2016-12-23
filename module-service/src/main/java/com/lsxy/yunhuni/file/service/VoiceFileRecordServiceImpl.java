package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.file.dao.VoiceFileRecordDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/21.
 */
@Service
public class VoiceFileRecordServiceImpl extends AbstractService<VoiceFileRecord> implements VoiceFileRecordService{
    @Autowired
    private VoiceFileRecordDao voiceFileRecordDao;
    @Override
    public BaseDaoInterface<VoiceFileRecord, Serializable> getDao() {
        return voiceFileRecordDao;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public long getSumSize(String tenant, String app) {
        String sql = "select IFNULL(sum(size),0) from db_lsxy_bi_yunhuni.tb_bi_voice_file_record where deleted=0 and tenant_id=? and app_id=?";
        return jdbcTemplate.queryForObject(sql,Long.class,tenant,app);
    }

    @Override
    public Page<VoiceFileRecord> pageList(Integer pageNo, Integer pageSize,String appId,String tenantId) {
        String hql = " from VoiceFileRecord obj where obj.appId=?1 and obj.tenantId=?2 ";
        Page<VoiceFileRecord> page = this.pageList(hql,pageNo,pageSize,appId,tenantId);
        return page;
    }

    @Override
    public Page<Map> getPageList(Integer pageNo, Integer pageSize, String appId, String tenantId, String type,Date start, Date end) {
        String sql = "FROM (SELECT a.id AS id,a.create_time AS time, a.session_code AS type,IFNULL(SUM(size),0) AS size,IFNULL(SUM(a.cost_time_long),0) AS costTimeLong,IFNULL(SUM(cost),0) AS cost ,a.tenant_id AS tenantId,a.app_id as appId ,a.deleted AS deleted FROM (SELECT * FROM  db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE deleted=0 ORDER BY create_time)a GROUP BY a.session_id )b WHERE b.deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql+=" AND b.appId='"+appId+"'";
        }
        if(StringUtils.isNotEmpty(tenantId)){
            sql+=" AND b.tenantId='"+tenantId+"'";
        }
        if(StringUtils.isNotEmpty(type)){
            String typeName = ProductCode.getApiCmdByRemark(type.trim());
            if(StringUtils.isNotBlank(tenantId)){
                sql+="AND b.type = '"+typeName+"'";
            }
        }
        if(start!=null&&end!=null){
            sql+="AND b.time BETWEEN ? AND ? ";
        }
        String count = "SELECT COUNT(1) "+sql;
        String sql2 =  "SELECT b.id as id ,b.time as time ,b.type as type ,b.size as size ,b.costTimeLong as costTimeLong,b.cost as cost  "+sql+ " LIMIT ?,?";
        long totalCount = 0;
        if(start!=null&&end!=null){
            totalCount = jdbcTemplate.queryForObject(count,Long.class,start,end);
        }else{
            totalCount = jdbcTemplate.queryForObject(count,Long.class);
        }
        pageNo--;
        int maxResults = pageSize;
        int firstResult = pageNo*pageSize;
        List<Map<String, Object>> rList = new ArrayList<>();
        if(start!=null&&end!=null){
            try {
                rList = jdbcTemplate.queryForList(sql2, start, end, firstResult, maxResults);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            rList = jdbcTemplate.queryForList(sql2,firstResult,maxResults);
        }
        List list = new ArrayList<>();
        if(rList!=null&&rList.size()>0) {
            for (int i=0;i<rList.size();i++){
                Map map = rList.get(i);
                map.put("type",ProductCode.valueOf((String)map.get("type")).getRemark());
                map.put("time",DateUtils.formatDate((Date)map.get("time"),"yyyy-MM-dd HH:mm:ss"));
                map.put("size",getSizeStr((Double) map.get("size")));
                list.add(map);

            }
        }
        Page page = new Page((pageNo)*pageSize+1,totalCount,pageSize,list);
        return page;
    }

    @Override
    public Map sumAndCount(String appId, String tenantId,String type,Date start,Date end) {
        String sql = "FROM (SELECT a.id AS id,a.create_time AS time, a.session_code AS type,IFNULL(SUM(size),0) AS size,IFNULL(SUM(a.cost_time_long),0) AS costTimeLong,IFNULL(SUM(cost),0) AS cost ,a.tenant_id AS tenantId,a.app_id as appId ,a.deleted AS deleted FROM (SELECT * FROM  db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE deleted=0 ORDER BY create_time)a GROUP BY a.session_id )b WHERE b.deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql+=" AND b.appId='"+appId+"'";
        }
        if(StringUtils.isNotEmpty(tenantId)){
            sql+=" AND b.tenantId='"+tenantId+"'";
        }
        if(StringUtils.isNotEmpty(type)){
            String typeName = ProductCode.getApiCmdByRemark(type);
            if(StringUtils.isNotBlank(typeName)){
                sql+="AND b.type = '"+typeName+"'";
            }
        }
        if(start!=null&&end!=null){
            sql+="AND b.time BETWEEN ? AND ? ";
        }
        String sql1 =  "SELECT IFNULL(sum(b.size),0) as size ,IFNULL(sum(b.cost ),0) as cost  "+sql;
        Map<String,Object> map = null;
        if(start!=null&&end!=null){
            try {
                map = jdbcTemplate.queryForMap(sql1, start, end);
            }catch ( Exception e){
                e.printStackTrace();
            }
        }else{
            map = jdbcTemplate.queryForMap(sql1);
        }
        map.put("size",getSizeStr((Double) map.get("size")));
        return map;
    }
    private String getSizeStr(Double size){
        String temp = "";
        DecimalFormat fmt=new DecimalFormat("0.##");
        if(size>1024*1024*1024){
            temp = fmt.format((size/1024/1024/1024))+"GB";
        }else if(size>(1024*1024)){
            temp = fmt.format((size/1024/1024))+"MB";
        }else if(size>1024){
            temp = fmt.format((size/1024))+"KB";
        }else{
            temp = fmt.format(size)+"B";
        }
        return temp;
    }

    @Override
    public List<VoiceFileRecord> getListBySessionId(String sessionId) {
        String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId in ( ?1)";
        List list = this.list(hql, sessionId);
        return list;
    }

    @Override
    public void batchUpdateAADelete(List<String> id, int status) {
        String ids = "";
        for(int i=0;i<id.size();i++){
            ids += "'"+id.get(i)+"'";
            if(i!=id.size()-1){
                ids+=",";
            }
        }
        String sql = "update db_lsxy_bi_yunhuni.tb_bi_voice_file_record set oss_deleted=? where id in (?)";
        jdbcTemplate.update(sql,status,ids);
    }

    @Override
    public List<Map> getAAListByCreateTimeAndTenantIdAndAreaId(Date createTime, String tenantId, String areaId) {
        String sql = " SELECT id,url FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND area_id=? AND create_time <=? AND aa_deleted<>1 ";
        List<Map> list = jdbcTemplate.queryForList(sql,Map.class,tenantId,areaId,createTime);
        return list;
    }

    @Override
    public List<String> getAAAreaByCreateTimeAndTenantId(Date createTime, String tenantId) {
        String sql = " SELECT DISTINCT area_id FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND create_time <=? AND aa_deleted<>1 ";
        List<String> list = jdbcTemplate.queryForList(sql,String.class,tenantId,createTime);
        return list;
    }
    @Override
    public List<VoiceFileRecord> getListByTenantAndAppAndCreateTime(String tenant, String app, Date createTime) {
        String hql = " FROM VoiceFileRecord obj WHERE obj.tenantId=?1 AND obj.appId=?2 AND obj.createTime<=?3";
        List list = this.list(hql, tenant,app,createTime);
        return list;
    }

    @Override
    public List<Map> getOssListByDeleted() {
        String sql = " SELECT id as id ,file_key as ossUrl FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE deleted=1 and sync==1 and file_key is not null and file_key<>'' and (oss_deleted is null or oss_deleted<>1)";
        List<Map> list = jdbcTemplate.queryForList(sql,Map.class);
        return list;
    }
}
