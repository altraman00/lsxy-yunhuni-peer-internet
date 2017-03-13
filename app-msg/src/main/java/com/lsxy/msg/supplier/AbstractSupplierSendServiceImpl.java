package com.lsxy.msg.supplier;

import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import com.lsxy.msg.supplier.common.ResultMass;
import com.lsxy.msg.supplier.common.ResultOne;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/8.
 */
public abstract class AbstractSupplierSendServiceImpl implements SupplierSendService {
    @Autowired
    MsgSupplierTemplateService msgSupplierTemplateService;


    @Override
    public ResultOne sendOne(String tempId, List<String> tempArgs, String msg, String mobile, String sendType) {
        return null;
    }

    @Override
    public ResultMass sendMass(String recordId,String tenantId, String appId, String subaccountId, String msgKey, String taskName, String tempId, List<String> tempArgs, String msg, List<String> mobiles, Date sendTime, String sendType, String cost) {
        return null;
    }

    @Override
    public int getMaxSendNum() {
        return 0;
    }

    protected String getSupplierTempId(String tempId,String supplierCode){
        return msgSupplierTemplateService.findSupplierTempIdByTempIdAndSupplierCode(tempId,supplierCode);
    }

}
