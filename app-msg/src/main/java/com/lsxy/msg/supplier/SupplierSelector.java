package com.lsxy.msg.supplier;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.msg.supplier.common.MsgConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by liups on 2017/3/8.
 */
@Service
public class SupplierSelector {
    @Autowired
    @Qualifier(value = "paoPaoYuService")
    SupplierSendService paoPaoYuService;

    @Autowired
    @Qualifier(value = "qiXunTongService")
    SupplierSendService qiXunTongService;

    public SupplierSendService getSendOneService(String operator,String sendType){
        return getSupplierSendService(operator, sendType);
    }

    private SupplierSendService getSupplierSendService(String operator, String sendType) {
        if(MsgConstant.MSG_USSD.equals(sendType)){
            switch(operator){
                case MsgConstant.ChinaMobile:{
                    return paoPaoYuService;
                }
            }
        }else if(MsgConstant.MSG_SMS.equals(sendType)){
            switch(operator){
                case MsgConstant.ChinaMobile:{
                    return paoPaoYuService;
                }
                case MsgConstant.ChinaUnicom:{
                    return qiXunTongService;
                }
            }
        }
        return null;
    }

    public SupplierSendService getSendMassService(String operator,String sendType){
        return getSupplierSendService(operator, sendType);
    }

}
