package com.lsxy.msg.supplier;

import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSupplier;
import com.lsxy.msg.api.model.MsgSupplierTemplate;
import com.lsxy.msg.api.service.MsgSupplierService;
import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    @Qualifier(value = "jiMuService")
    SupplierSendService jiMuService;
    @Autowired
    MsgSupplierTemplateService msgSupplierTemplateService;
    @Autowired
    MsgSupplierService msgSupplierService;

    public SupplierSendService getSendOneService(String operator,String sendType,String tempId){
        return getSupplierSendService(operator, sendType,tempId,MsgSupplier.IS_SINGLE);
    }

    public SupplierSendService getSendMassService(String operator,String sendType,String tempId){
        return getSupplierSendService(operator, sendType,tempId,MsgSupplier.IS_MASS);
    }
    public SupplierSendService getSupplierSendService(String code) {
        switch (code){
            case MsgSupplier.JiMuCode:{return jiMuService;}
            case MsgSupplier.QixuntongCode:{return qiXunTongService;}
            case MsgSupplier.PaopaoyuCode:{return paoPaoYuService;}
        }
        return null;
    }
    private SupplierSendService getSupplierSendService(String operator, String sendType,String tempId,int isMass) {
        List<MsgSupplierTemplate> list = msgSupplierTemplateService.findByTempIdAndStatus(tempId,MsgSupplierTemplate.STATUS_PASS);
        if(list == null || list.size()==0){
            return null;
        }
        Map<String,MsgSupplierTemplate> map = new HashMap<String,MsgSupplierTemplate>();
        List<String> codes = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            codes.add(list.get(i).getSupplierCode());
            map.put(list.get(i).getSupplierCode(),list.get(i));
        }
        List<MsgSupplier> msgSupplierList = msgSupplierService.findByCodeAndOperatorAndSendTypeAndIsMassAndOrder(codes,operator,sendType,isMass,null);

        if(msgSupplierList == null && msgSupplierList.size()==0){
            return null;
        }
        //目前只取第一个供应商来使用
        MsgSupplier msgSupplier = msgSupplierList.get(0);
        String code = msgSupplier.getCode();
        return getSupplierSendService(code);
    }

}
