package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/9/2.
 */
public interface TelnumToLineGatewayDao extends BaseDaoInterface<TelnumToLineGateway, Serializable> {
    /**
     * 根据号码来选择一条线路
     * @param telnum
     * @return
     */
    TelnumToLineGateway findFirstByTelNumber(String telnum);


    /**
     * 获取这个号码的所有可呼出线路
     * @param number
     * @return
     */
    @Query("select ttg from TelnumToLineGateway ttg where ttg.telNumber=?1 and (ttg.isDialing=1 or ttg.isThrough=1)")
    List<TelnumToLineGateway> findDialingLine(String number);

    /**
     * 获取被叫线路
     * @param telNumber
     * @param isCalled
     * @return
     */
    TelnumToLineGateway findFirstByTelNumberAndIsCalled(String telNumber, String isCalled);

    TelnumToLineGateway findByTelNumberAndLineId(String telNumber,String lineId);
}
