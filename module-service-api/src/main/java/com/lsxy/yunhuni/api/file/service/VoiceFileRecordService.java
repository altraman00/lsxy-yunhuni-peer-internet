package com.lsxy.yunhuni.api.file.service;

import com.lsxy.framework.api.base.BaseService;
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
    /**
     * 根据文件名查找文件分页数据
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    Page<VoiceFileRecord> pageList(Integer pageNo, Integer pageSize, String appId, String tenantId);

    /**
     * 统计存储文件
     * @param appId
     * @param tenantId
     * @return
     */
    Map sumAndCount(String appId, String tenantId, Date startTime,Date endTime);
    /**
     * 批量更新删除状态为删除
     * @param appid
     * @param tenantId
     * @param startTime
     * @param endTime
     * @return
     */
    int batchDelete(String appid, String tenantId, Date startTime,Date endTime);

    /**
     * 获取数据
     * @param appid
     * @param tenantId
     * @param startTime
     * @param endTime
     * @return
     */
    List<VoiceFileRecord> list(String appid, String tenantId, Date startTime,Date endTime);

    List<VoiceFileRecord> getListDistinctUrl(String ...sessionId);
}
