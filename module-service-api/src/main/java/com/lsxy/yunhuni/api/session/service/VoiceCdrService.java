package com.lsxy.yunhuni.api.session.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;

import java.math.BigDecimal;

/**
 * 通话记录
 * Created by zhangxb on 2016/7/19.
 */
public interface VoiceCdrService extends BaseService<VoiceCdr> {
    /**
     * 根据类型，日期，应用id来查询获取分页数据
     */
    public Page<VoiceCdr> pageList(Integer pageNo,Integer pageSize,Integer type,String tenantId,String time,String appId);

    /**
     * 根据查询类型，应用id，统计当前日期的消费额度或者验证码条数
     * @param type 类型 会话类型1.语音呼叫 2.双向回拨 3.会议 4.IVR定制服务  5.语音验证码 6.录音
     * @param time 时间格式 yyyy-MM-dd
     * @param appId 应用id
     * @return
     */
    public BigDecimal sumCost(Integer type, String tenantId, String time, String appId);
}
