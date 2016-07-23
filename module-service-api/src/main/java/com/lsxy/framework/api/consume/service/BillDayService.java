package com.lsxy.framework.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.consume.model.BillDay;

import java.util.List;

/**
 * 日结账单
 * Created by liups on 2016/7/22.
 */
public interface BillDayService extends BaseService<BillDay> {
    /**
     * 获取某一日的日结账单
     * @param userName
     * @param appId
     * @param day
     * @return
     */
    List<BillDay> getBillDays(String userName, String appId, String day);
}
