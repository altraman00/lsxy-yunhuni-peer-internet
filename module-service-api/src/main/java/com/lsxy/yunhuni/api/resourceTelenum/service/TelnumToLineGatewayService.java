package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;

import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/9/2.
 */
public interface TelnumToLineGatewayService extends BaseService<TelnumToLineGateway> {

    /**
     * 根据号码选择线路ID
     * @param number
     * @return
     */
    List<TelnumToLineGateway> getDialingLinesByNumber(String number);

    LineGateway getCalledLineByNumber(String number);

    List<TelnumToLineGateway> getListByLine(String line);
    /**
     * 根据线路逻辑上删除
     *
     * @param line
     */
    void deleteByLineId(String line);

    /**
     * 根据号码逻辑上删除
     *
     * @param telnum
     */
    void deleteByTelnum(String telnum);

    /**
     * 根据号码选择线路ID
     *
     * @param number
     * @return
     */
    Page<TelnumToLineGateway> getPage(Integer pageNo, Integer pageSize, String line, String number, String isDialing, String isCalled, String isThrough);
    /**
     * 根据号码选择线路ID
     *
     * @param number
     * @return
     */
    Page<TelnumToLineGateway> getIsNotNullPage(Integer pageNo, Integer pageSize, String isNotNull, String number);
    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelete(String line, String[] ids);

    /**
     * 透传-批量增加
     */
    void batchInsert(String id, String provider, String[] ids);

    /**
     * 获取号码是否可呼叫
     */
    Map getTelnumCall(String telnum, String line);

    /**
     * 获取线路上的号码
     */
    List<String> getTelnumByLineId(String line);

    /**
     * 根据线路修改是否可透传
     *
     * @param line
     * @param isThrough
     */
    void updateIsThrough(String line, String isThrough);

    /**
     * 根据号码和线路获取对象
     *
     * @param telNumber
     * @param lineId
     * @return
     */
    TelnumToLineGateway findByTelNumberAndLineId(String telNumber, String lineId);

    /**
     * 修改号码
     * @param telnum
     */
    void updateTelnum(String telnum1,String telnum);


    void batchUpCall(String line,String... nums);

    void modify(LineGateway lineGateway,String isThrough1,String isThrough2);

    void deleteLine(String id);

    void telnumDelete(String id,String[] ids,String[] idss);

    void telnumCreate(ResourceTelenum resourceTelenum,TelnumToLineGateway telnumToLineGateway);

    int getIsThrough(String telnum);
}
