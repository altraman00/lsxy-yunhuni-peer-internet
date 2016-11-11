package com.lsxy.yunhuni.file.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.file.dao.VoiceFileRecordDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
    @Autowired
    VoiceCdrService voiceCdrService;
    @Autowired
    MeetingMemberService meetingMemberService;

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
    public List<VoiceFileRecord> list(String appid, String tenantId, Date startTime, Date endTime) {
        String hql = " from VoiceFileRecord obj where obj.app_id=?1 and obj.tenant_id=?2 and obj.createTime<=?3 and obj.createTime>=?4";
        List<VoiceFileRecord> list = this.list(hql,appid,tenantId,endTime,startTime);
        return list;
    }

    @Override
    public List<VoiceFileRecord> getListByCdrId(String id) {
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        if(voiceCdr!=null&&StringUtils.isNotEmpty(voiceCdr.getId())){
            ProductCode p1 = ProductCode.changeApiCmdToProductCode(voiceCdr.getType());
            //语音会议
            if(ProductCode.sys_conf.getRemark().equals(p1.getRemark())){
                //获取会议操作者
                MeetingMember meetingMember = meetingMemberService.findById(voiceCdr.getSessionId());
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用会议id
                List list= this.list(hql,meetingMember.getMeeting().getId());
                return list;
            }
            //自定义IVR
            else if(ProductCode.ivr_call.getRemark().equals(p1.getRemark())){
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用ivr的id
                List list= this.list(hql,voiceCdr.getSessionId());
                return list;
            }
            //语音回拔
            else if(ProductCode.duo_call.getRemark().equals(p1.getRemark())){
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用双向回拨的id
                List list= this.list(hql,voiceCdr.getSessionId());
                return list;
            }
            //呼叫中心
            else if(ProductCode.call_center.getRemark().equals(p1.getRemark())){
                //根据sessionid获取呼叫中心交互成员，在获取呼叫中心交谈，在获取文件
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用ivr的id
                List list= this.list(hql,voiceCdr.getSessionId());
                return list;
            }
        }
        return null;
    }

}
