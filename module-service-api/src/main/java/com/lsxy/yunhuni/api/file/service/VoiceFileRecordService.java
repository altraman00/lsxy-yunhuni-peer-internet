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
    /** 根据租户和用户获取对应录音文件的总大小*/
    long getSumSize(String tenant,String app);
    /** 根据租户和用户和时间区间获取统计存储文件*/
    Map sumAndCount(String appId, String tenantId,String type,Date start,Date end);
    /** 根据文件名查找文件分页数据*/
    Page<VoiceFileRecord> pageList(Integer pageNo, Integer pageSize, String appId, String tenantId);
    Page<Map> getPageList(Integer pageNo, Integer pageSize, String appId, String tenantId, String type,Date start, Date end);
    /** 批量更新删除状态为删除*/
    int batchDelete(String appid, String tenantId, Date startTime,Date endTime);
    /** 获取数据*/
    List<VoiceFileRecord> getList(String appid, String tenantId, Date startTime,Date endTime);
    List<VoiceFileRecord> getListBySessionId(String sessionId);
    void batchUpdateAADelete(List<String> id,int status);
    /** 根据创建时间，租户，区域获取录音文件AA文件存在*/
    List<Map> getAAListByCreateTimeAndTenantIdAndAreaId(Date createTime, String tenantId,String areaId);
    /** 根据创建时间，租户，区域获取AA文件存在的区域*/
    List<String> getAAAreaByCreateTimeAndTenantId(Date createTime,String tenantId);
}
