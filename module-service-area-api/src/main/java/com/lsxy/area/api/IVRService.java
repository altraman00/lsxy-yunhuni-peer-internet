package com.lsxy.area.api;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by liuws on 2016/9/1.
 */
public interface IVRService {

    public String ivrCall(String subaccountId,String ip,String appId,String from,String to,Integer maxDialDuration,
                          Integer maxCallDuration, String userData) throws YunhuniApiException;
}
