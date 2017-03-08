package com.lsxy.msg.supplier;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.msg.supplier.model.MsgConstant;
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

    public SupplierSendService getUssdSendOneService(String operator){
        switch(operator){
            case MsgConstant.ChinaMobile:{
                return paoPaoYuService;
            }
        }
        return null;
    }

    public SupplierSendService getUssdSendMassService(String operator){
        switch(operator){
            case MsgConstant.ChinaMobile:{
                return paoPaoYuService;
            }
        }
        return null;
    }

    public SupplierSendService getSmsSendOneService(String operator){
        switch(operator){
            case MsgConstant.ChinaMobile:{
                return paoPaoYuService;
            }
            case MsgConstant.ChinaUnicom:{
                return qiXunTongService;
            }
        }
        return null;
    }

    public SupplierSendService getSmsSendMassService(String operator){
        switch(operator){
            case MsgConstant.ChinaMobile:{
                return paoPaoYuService;
            }
            case MsgConstant.ChinaUnicom:{
                return qiXunTongService;
            }
        }
        return null;
    }
}
