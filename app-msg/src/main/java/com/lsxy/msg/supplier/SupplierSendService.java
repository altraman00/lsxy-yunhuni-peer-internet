package com.lsxy.msg.supplier;

import com.lsxy.msg.supplier.model.PaoPaoYuMassNofity;
import com.lsxy.msg.supplier.model.ResultMass;
import com.lsxy.msg.supplier.model.ResultOne;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/8.
 */
public interface SupplierSendService {
    ResultOne ussdSendOne(String tempId, List<String> tempArgs, String msg, String mobile);

    ResultMass ussdSendMass(String taskName,String tempId,List<String> tempArgs,String msg, List<String> mobiles,Date sendTime);

    ResultOne smsSendOne(String tempId,List<String> tempArgs,String msg,String mobile);

    ResultMass smsSendMass(String taskName,String tempId,List<String> tempArgs,String msg, List<String> mobiles,Date sendTime);

    PaoPaoYuMassNofity getTask(String taskId);

    String getSupplierCode();

    int getMaxSendNum();
}
