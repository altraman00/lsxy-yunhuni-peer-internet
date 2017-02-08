package com.lsxy.yunhuni.api.session.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通话记录
 * Created by zhangxb on 2016/7/19.
 */
public interface VoiceCdrService extends BaseService<VoiceCdr> {
    /**
     * 根据类型，日期，应用id来查询获取数据
     */
    public List<VoiceCdr> listCdr(String type, String tenantId, String time, String appId);

    /**
     * 根据类型，日期，应用id来查询获取分页数据
     */
    public Page<VoiceCdr> pageList(Integer pageNo,Integer pageSize,String type,String tenantId,String time,String appId);
    public Page<VoiceCdr> pageList(Integer pageNo,Integer pageSize,String type,String tenantId,String time1,String time2,String appId);
    /**
     * 根据查询类型，应用id，统计当前日期的消费额度或者验证码条数
     * @param type 类型 查看产品表code字段或枚举类ProductCode
     * @param time 时间格式 yyyy-MM-dd
     * @param appId 应用id
     * @return
     */
    public Map sumCost(String type, String tenantId, String time, String appId);
    public Map sumCost(String type, String tenantId, String time1,String time2, String appId);
    /**
     * 获取应用呼叫状况
     * @param appId
     * @return
     */
    Map currentRecordStatistics(String appId);

    /**
     * 根据租户id，应用id，和时间区间获取平均通话时长（分钟），接通率，消费金额，会话量，话务量（分钟）
     * @param tenantId
     * @param appId
     * @param startTime
     * @param endTime
     * @return
     */
//    Map getAvgCdr(String tenantId, String appId, String startTime, String endTime);


    /**
     * 根据租户id，应用id，和时间区间获取接通量，不接通量，话务量（秒）
     * @param tenantId
     * @param appId
     * @param startTime
     * @param endTime
     * @return
     */
    Map getStaticCdr(String tenantId, String appId, Date startTime, Date endTime);

    void insertCdr(VoiceCdr voiceCdr);
}
