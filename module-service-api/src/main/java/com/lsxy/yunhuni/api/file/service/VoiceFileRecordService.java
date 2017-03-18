package com.lsxy.yunhuni.api.file.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/21.
 */
public interface VoiceFileRecordService extends BaseService<VoiceFileRecord> {
    /** 根据租户,应用,类型,和时间区间来获取录音文件的列表数据*/
    Page<Map> getPageList(Integer pageNo, Integer pageSize, String appId, String tenantId, String type,Date start, Date end);
    /** 根据租户,应用,类型,和时间区间来获取统计存储文件*/
    Map sumAndCount(String appId, String tenantId,String type,Date start,Date end);

    /** 根据租户,应用获取应用*/
    Page<VoiceFileRecord> pageList(Integer pageNo, Integer pageSize, String appId, String tenantId);

    /** 根据租户和用户获取对应录音文件的总大小*/
    long getSumSize(String tenant,String app);

    /**根据sessionid获取录音文件*/
    List<VoiceFileRecord> getListBySessionId(String... sessionId);
    /** 根据创建时间，租户，区域获取AA文件存在的区域*/
    List<String> getAAAreaByCreateTimeAndTenantId(Date createTime,String tenantId);
    /** 根据创建时间，租户，区域获取录音文件AA文件存在*/
    List<Map> getAAListByCreateTimeAndTenantIdAndAreaId(Date createTime, String tenantId,String areaId);
    /** 根据记录id来更新区域文件的删除状态*/
    void batchUpdateAADelete(List<String> id,int status);
    /** 根据租户，应用，获取截止到指定开始时间的录音文件*/
    List<VoiceFileRecord> getListByTenantAndAppAndCreateTime(String tenant,String app,Date createTime);
    /** 获取已删除记录但没有删除oss文件的记录*/
    List<Map> getOssListByDeleted();

    void insertRecord(VoiceFileRecord record);

    /**
     * 录音容量月租扣费
     */
    void recordingVoiceFileTask();

    /** 对录音进行扣费*/
    boolean recordCost(String tenantId,String appId);

    Page<VoiceFileRecord> getPageListForGW(String appId, String subaccountId, Date start, Date end, Integer pageNo, Integer pageSize);

    Integer setWaitedUpload(List<String> ids);
}
