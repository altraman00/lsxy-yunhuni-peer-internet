package com.lsxy.msg.supplier;

import com.lsxy.msg.supplier.model.PaoPaoYuMassNofity;
import com.lsxy.msg.supplier.model.ResultMass;
import com.lsxy.msg.supplier.model.ResultOne;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/8.
 */
public class AbstractSupplierSendServiceImpl implements SupplierSendService {

    @Override
    public ResultOne ussdSendOne(String tempId, List<String> tempArgs, String msg, String mobile) {
        return null;
    }

    @Override
    public ResultMass ussdSendMass(String taskName, String tempId, List<String> tempArgs, String msg, List<String> mobiles, Date sendTime) {
        return null;
    }

    @Override
    public ResultOne smsSendOne(String tempId, List<String> tempArgs, String msg, String mobile) {
        return null;
    }

    @Override
    public ResultMass smsSendMass(String taskName, String tempId, List<String> tempArgs, String msg, List<String> mobiles, Date sendTime) {
        return null;
    }

    @Override
    public PaoPaoYuMassNofity getTask(String taskId) {
        return null;
    }
}
