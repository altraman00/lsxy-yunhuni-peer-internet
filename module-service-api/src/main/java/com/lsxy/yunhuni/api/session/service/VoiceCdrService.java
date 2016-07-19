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
    public Page<VoiceCdr> pList(Integer pageNo,Integer pageSize,String tenantId,Integer type,String time,String appId);

    public BigDecimal sumCost(String tenantId, Integer type, String time, String appId);
}
