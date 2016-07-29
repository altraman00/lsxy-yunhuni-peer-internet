package com.lsxy.framework.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.consume.model.BillMonth;

import java.util.List;

/**
 * 月结账单Service
 * Created by liups on 2016/7/11.
 */
public interface BillMonthService extends BaseService<BillMonth> {

    List<BillMonth> getBillMonths(String userName, String appId, String month);

}