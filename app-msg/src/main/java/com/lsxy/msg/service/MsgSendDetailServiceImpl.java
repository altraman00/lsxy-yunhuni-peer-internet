package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.dao.MsgSendDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendDetailServiceImpl extends AbstractService<MsgSendDetail> implements MsgSendDetailService {
    @Autowired
    MsgSendDetailDao msgSendDetailDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Override
    public BaseDaoInterface<MsgSendDetail, Serializable> getDao() {
        return this.msgSendDetailDao;
    }

    @Override
    public List<String> batchInsertDetail(MsgSendRecord record, List<String> phones,int state) {
        String values = " id,msg_key,tenant_id,app_id,subaccount_id,task_id,record_id,mobile,msg,is_mass,temp_id,supplier_temp_id,temp_args,send_time,msg_cost,send_type,supplier_code,operator,state," +
                "create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }
        String insertSql = " insert into db_lsxy_bi_yunhuni.tb_bi_voice_cdr_month("+ values + ") values ("+valuesMark+")";
        List resultList = new ArrayList();
        List<String> ids = new ArrayList<>();
        if(phones != null && phones.size() > 0){
            for(String phone:phones){
                String uuid = UUIDGenerator.uuid();
                ids.add(uuid);
                Object[] obj =  new Object[]{uuid,record.getMsgKey(),record.getTenantId(),record.getAppId(),record.getSubaccountId(),record.getTaskId(),record.getId(),phone,record.getMsg(),true,
                record.getTempId(),record.getSupplierTempId(),record.getTempArgs(),record.getSendTime(),record.getMsgCost(),record.getSendType(),record.getSupplierCode(),record.getOperator(),
                        state,record.getCreateTime(),record.getLastTime(),0,0,0};
                resultList.add(obj);
            }
        }

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
        return ids;

    }

    @Override
    public void updateStateByMsgKey(String msgKey, int state) {
        msgSendDetailDao.updateStateByMsgKey(msgKey,state);
    }

    @Override
    public MsgSendDetail findByTaskIdAndMobile(String taskId, String mobile) {
        return msgSendDetailDao.findFirstByTaskIdAndMobile(taskId,mobile);
    }

    @Override
    public List<String> updateStateAndTaskIdByRecordIdAndPhones(String recordId, List<String> phones, int state, String taskId) {
        msgSendDetailDao.updateDetailStateAndTaskIdByRecordId(recordId, phones, state,taskId);
        if(MsgSendDetail.STATE_FAIL == state){
             return msgSendDetailDao.findIdByRecordIdAndMobileIn(recordId,phones);
        }else{
            return null;
        }
    }

    @Override
    public List<String> updateStateByRecordIdAndPhones(String recordId, List<String> phones, int state) {
        msgSendDetailDao.updateStateByRecordId(recordId, phones, state);
        if(MsgSendDetail.STATE_FAIL == state){
            return msgSendDetailDao.findIdByRecordIdAndMobileIn(recordId,phones);
        }else{
            return null;
        }
    }

    @Override
    public void updateStateFromWaitedToSuccessByRecordId(String recordId) {
        msgSendDetailDao.updateStateFromWaitedToStateByRecordId(recordId, MsgSendDetail.STATE_WAIT ,MsgSendDetail.STATE_SUCCESS);
    }

    @Override
    public Map getStateCountByRecordId(String recordId) {
        String sql = "SELECT d.state,COUNT(1) FROM db_lsxy_bi_yunhuni.tb_bi_msg_send_detail d WHERE d.record_id = ? GROUP BY d.state";
        return jdbcTemplate.queryForMap(sql,recordId);
    }


    @Override
    public Page<MsgSendDetail> getPageByContiton(Integer pageNo, Integer pageSize, String msgKey, String mobile, String state) {
        String hql = " from MsgSendDetail obj where obj.msgKey=?1 ";
        if(StringUtils.isNotEmpty(mobile)){
            hql += " and obj.mobile like '%"+mobile+"%' ";
        }
        if(StringUtils.isNotEmpty(state)){
            hql += " and obj.state = '"+state+"' ";
        }
        return pageList(hql,pageNo,pageSize,msgKey);
    }

    @Override
    public List<MsgSendDetail> findByMsgKey(String msgKey) {
        return msgSendDetailDao.findByMsgKey(msgKey);
    }

    @Override
    public void setEndTimeByMsgKey(String msgKey) {
        msgSendDetailDao.setEndTimeByMsgKey(msgKey,new Date());
    }
}
