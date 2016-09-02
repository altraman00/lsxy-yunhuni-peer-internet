package com.lsxy.framework.api.invoice.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;

import java.util.Map;

/**
 * 发票申请Service
 * Created by liups on 2016/7/21.
 */
public interface InvoiceApplyService extends BaseService<InvoiceApply> {
    /**
     * 获取能开发票的开始时间
     * @return
     */
    String getStart(String tenantId);

    /**
     * 获取发票申请记录
     * @param tenantId
     * @param pageNo 当前页
     * @param pageSize 每一页的数量
     * @return
     */
    Page<InvoiceApply> getPage(String tenantId, Integer pageNo, Integer pageSize,Integer operate);

    /**
     * 插入新的发票申请
     * @param apply
     * @param userName
     * @return
     */
    InvoiceApply create(InvoiceApply apply, String userName);

    /**
     * 根据条件查找分页信息
     * @param pageNo   第几页
     * @param pageSize 每页记录数
     * @param tenantId 租户集合
     * @param status 发票状态
     * @param type 发票类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page<InvoiceApply> pageList(Integer pageNo, Integer pageSize,Integer type, String[] tenantId, Integer status, String startTime, String endTime);

    /**
     * 根据条件查找分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param tenantId 租户集合
     * @param status 发票状态
     * @param type 发票类型
     * @return
     */
    Page<InvoiceApply> pageList(Integer pageNo, Integer pageSize, String[] tenantId, Integer status, Integer type,boolean flag);
    /**
     * 获取未处理的记录数
     * @return
     */
    Map getAwaitNum();
}
