package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.MsgDay;

import java.util.Date;

/**
 * Created by liups on 2017/3/14.
 */
public interface MsgDayService extends BaseService<MsgDay> {
    void dayStatistics(Date date);
}
