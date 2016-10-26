package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;

import java.util.List;

/**
 * Created by liups on 2016/9/2.
 */
public interface TelnumToLineGatewayService extends BaseService<TelnumToLineGateway> {
    /**
     * 根据号码选择区域
     * @param telnum
     * @return
     */
    String getAreaIdByTelnum(String telnum);

    /**
     * 根据号码选择线路ID
     * @param number
     * @return
     */
    List<String> getLineIdsByNumber(String number);

    /**
     * 根据线路逻辑上删除
     * @param line
     */
    void deleteByLineId(String line);
    /**
     * 根据号码选择线路ID
     * @param number
     * @return
     */
    Page<TelnumToLineGateway> getPage(Integer pageNo,Integer pageSize,String number,String isDialing,String isCalled,String isThrough);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(String[] ids);


}
