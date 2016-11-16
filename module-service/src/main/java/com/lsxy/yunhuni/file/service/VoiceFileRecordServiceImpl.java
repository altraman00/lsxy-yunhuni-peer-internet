package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.file.dao.VoiceFileRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
    public Page<VoiceFileRecord> pageList(Integer pageNo, Integer pageSize,String appId,String tenantId) {
        String hql = " from VoiceFileRecord obj where obj.appId=?1 and obj.tenantId=?2 ";
        Page<VoiceFileRecord> page = this.pageList(hql,pageNo,pageSize,appId,tenantId);
        return page;
    }

    @Override
    public Map sumAndCount(String appId, String tenantId,Date startTime,Date endTime) {
        String sql = "select COALESCE(sum(size),0) as size,count(1) as total from db_lsxy_bi_yunhuni.tb_bi_voice_file_record where deleted=0 and  app_id=? and tenant_id=? ";
        Map map = null;
        if(startTime!=null&&endTime!=null){
            sql = "select COALESCE(sum(size),0) as size,count(1) as total from db_lsxy_bi_yunhuni.tb_bi_voice_file_record where deleted=0 and  app_id=? and tenant_id=? and create_time<=? and create_time>=?";
            map = jdbcTemplate.queryForMap(sql,appId,tenantId,endTime,startTime);
        }else{
            map = jdbcTemplate.queryForMap(sql,appId,tenantId);
        }
        return map;
    }

    @Override
    public int batchDelete(String appid, String tenantId, Date startTime, Date endTime) {
        String sql = "update db_lsxy_bi_yunhuni.tb_bi_voice_file_record set deleted=1 where  deleted=0 and app_id=? and tenant_id=? and create_time<=? and create_time>=?";
        int result = jdbcTemplate.update(sql,appid,tenantId,endTime,startTime);
        return result;
    }

    @Override
    public List<VoiceFileRecord> getList(String appid, String tenantId, Date startTime, Date endTime) {
        String hql = " from VoiceFileRecord obj where obj.app_id=?1 and obj.tenant_id=?2 and obj.createTime<=?3 and obj.createTime>=?4";
        List<VoiceFileRecord> list = this.list(hql,appid,tenantId,endTime,startTime);
        return list;
    }

    @Override
    public List<VoiceFileRecord> getListBySessionId(String sessionId) {
        String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId in ( ?1)";
        List list = this.list(hql, sessionId);
        return list;
    }

    @Override
    public void batchUpdateOssDelete(List<String> id, int status) {
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
    public void batchDelete(Date createTime, String tenantId) {
        String sql = " update db_lsxy_bi_yunhuni.tb_bi_voice_file_record set deleted=1 WHERE deleted=0 AND tenant_id= ? AND create_time <=? ";
        jdbcTemplate.update(sql,createTime,tenantId);
    }

    @Override
    public List<Map> getOSSListByCreateTimeAndTenantIdAndAreaId(Date createTime, String tenantId, String areaId) {
        String sql = " SELECT id,oss_url AS ossUrl FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND area_id=? AND create_time <=? AND status=1 AND oss_deleted<>1 ";
        List<Map> list = jdbcTemplate.queryForList(sql,Map.class,tenantId,areaId,createTime);
        return list;
    }

    @Override
    public List<Map> getAAListByCreateTimeAndTenantIdAndAreaId(Date createTime, String tenantId, String areaId) {
        String sql = " SELECT id,url FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND area_id=? AND create_time <=? AND aa_deleted<>1 ";
        List<Map> list = jdbcTemplate.queryForList(sql,Map.class,tenantId,areaId,createTime);
        return list;
    }

    @Override
    public List<String> getOssAreaByCreateTimeAndTenantId(Date createTime, String tenantId) {
        String sql = " SELECT DISTINCT area_id FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND create_time <=? AND status=1 AND oss_deleted<>1";
        List<String> list = jdbcTemplate.queryForList(sql,String.class,tenantId,createTime);
        return list;
    }

    @Override
    public List<String> getAAAreaByCreateTimeAndTenantId(Date createTime, String tenantId) {
        String sql = " SELECT DISTINCT area_id FROM db_lsxy_bi_yunhuni.tb_bi_voice_file_record WHERE tenant_id= ? AND create_time <=? AND aa_deleted<>1 ";
        List<String> list = jdbcTemplate.queryForList(sql,String.class,tenantId,createTime);
        return list;
    }

    @Override
    public List<VoiceFileRecord> getListByCreateTimeAndTenantId(Date createTime, String tenantId) {
        String hql = " from  VoiceFileRecord obj where obj.createTime<=?1 and obj.tenant.id=?2 ";
        Page page = this.pageList(hql,1,20,createTime,tenantId);
        List<VoiceFileRecord> list = new ArrayList<>();
        while(page.getCurrentPageNo()<page.getTotalPageCount()){
            list.addAll(page.getResult());
            int index = Integer.valueOf(page.getCurrentPageNo()+"");
            page = this.pageList(hql,index+1,20,createTime,tenantId);
        }
        return list;
    }


}
