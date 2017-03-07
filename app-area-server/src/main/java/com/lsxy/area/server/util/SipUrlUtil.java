package com.lsxy.area.server.util;

import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/3/2.
 */
public class SipUrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(SipUrlUtil.class);

    private SipUrlUtil(){}

    public static String extractTelnum(String sip){
        if(StringUtil.isEmpty(sip)){
            return "";
        }
        int index = sip.indexOf("@");
        if(index <=0){
            index = sip.length();
        }
        if(logger.isDebugEnabled()){
            logger.info("{}====》{}",sip,sip.substring(0,index).replace("sip:",""));
        }
        return sip.substring(0,index).replace("sip:","");
    }
}
