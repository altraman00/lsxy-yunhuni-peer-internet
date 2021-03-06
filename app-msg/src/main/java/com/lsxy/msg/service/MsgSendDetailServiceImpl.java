package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.dao.MsgSendDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

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
        String insertSql = " insert into db_lsxy_bi_yunhuni.tb_bi_msg_send_detail("+ values + ") values ("+valuesMark+")";
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
    public MsgSendDetail findByTaskIdAndMobile(String taskId, String mobile) {
        return msgSendDetailDao.findFirstByTaskIdAndMobile(taskId,mobile);
    }

    @Override
    public List<String> updateStateAndTaskIdAndEndTimeByRecordIdAndPhones(String recordId, List<String> phones, int state, String taskId,Date endTime) {
        if(StringUtils.isBlank(recordId) || phones == null || phones.size() == 0){
            return null;
        }
        msgSendDetailDao.updateDetailStateAndTaskIdByRecordId(recordId, phones, state,taskId,endTime);
        if(MsgSendDetail.STATE_FAIL == state){
            List<MsgSendDetail> details = msgSendDetailDao.findByRecordIdAndMobileIn(recordId, phones);
            if(details != null && details.size()>0){
                List<String> ids = new ArrayList<>();
                for(MsgSendDetail detail : details){
                    ids.add(detail.getId());
                }
                return ids;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public List<String> updateStateAndSetEndTimeByRecordIdAndPhones(String recordId, List<String> phones, int state,Date endTime) {
        if(StringUtils.isBlank(recordId) || phones == null || phones.size() == 0  || endTime == null){
            return null;
        }
        msgSendDetailDao.updateStateByRecordId(recordId, phones, state,endTime);
        if(MsgSendDetail.STATE_FAIL == state){
            List<MsgSendDetail> details = msgSendDetailDao.findByRecordIdAndMobileIn(recordId, phones);
            if(details != null && details.size()>0){
                List<String> ids = new ArrayList<>();
                for(MsgSendDetail detail : details){
                    ids.add(detail.getId());
                }
                return ids;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public void updateStateAndSetEndTimeFromWaitedToSuccessByRecordId(String recordId,Date endTime) {
        msgSendDetailDao.updateStateFromWaitedToSuccessAndSetEndTimeByRecordId(recordId, MsgSendDetail.STATE_WAIT ,MsgSendDetail.STATE_SUCCESS,endTime);
    }

    @Override
    public Long finishOverdueRecordId(String recordId,Date endTime){
        msgSendDetailDao.updateStateFromWaitedToSuccessAndSetEndTimeByRecordId(recordId, MsgSendDetail.STATE_WAIT ,MsgSendDetail.STATE_SUCCESS,endTime);
        return msgSendDetailDao.countByRecordIdAndState(recordId,MsgSendDetail.STATE_FAIL);
    }

    @Override
    public Map getStateCountByRecordId(String recordId) {
        String sql = "SELECT d.state AS state,COUNT(1) AS count FROM db_lsxy_bi_yunhuni.tb_bi_msg_send_detail d WHERE d.record_id = ? GROUP BY d.state";
        Map<Integer,Long> result = new HashMap<>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,recordId);
        if(list != null && list.size() >0){
            for(Map map : list){
                result.put((Integer)map.get("state"),(Long)map.get("count"));
            }
        }
        return result;
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
    public String findFailMobilesByMsgKey(String msgKey) {
        String result = null;
        List<MsgSendDetail> failMobiles = msgSendDetailDao.findByMsgKeyAndState(msgKey,MsgSendDetail.STATE_WAIT);
        if(failMobiles != null && failMobiles.size() > 0){
            List<String> strings = new ArrayList<>();
            for(MsgSendDetail detail : failMobiles){
                strings.add(detail.getMobile());
            }
            result = StringUtils.join(strings,MsgConstant.NumRegexStr);
        }
        return result;
    }

}
