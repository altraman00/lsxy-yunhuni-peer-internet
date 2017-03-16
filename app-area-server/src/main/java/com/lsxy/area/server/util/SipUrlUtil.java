package com.lsxy.area.server.util;

import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.exceptions.api.NumberNotAllowToCallException;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

/**
 * Created by liuws on 2017/3/2.
 */
public class SipUrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(SipUrlUtil.class);

    private static final String hotNum = "66666666666";

    private static final Pattern EXTENSION_PATERN = Pattern.compile("^([2-9]\\d{6,10})|(100\\d{4,8})$");

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

    /***
     * 判断是不是分机号
     * @param uri
     * @return
     */
    public static boolean isExtensionNum(String uri){
        String telnum = extractTelnum(uri);
        return EXTENSION_PATERN.matcher(telnum).find();
    }

    /**
     * 判断是否是分机短号
     * @param uri
     * @return
     */
    public static boolean isShortNum(String prefix,String uri){
        String telnum = prefix + extractTelnum(uri);
        return isExtensionNum(telnum);
    }

    /**
     * 判断是不是热线号码
     * @param uri
     * @return
     */
    public static boolean isHotNum(String uri){
        String telnum = extractTelnum(uri);
        return hotNum.equals(telnum);
    }

    /**
     * 判断是不是外线号码
     * @param uri
     * @return
     */
    public static boolean isOut(String uri){
        String telnum = extractTelnum(uri);
        if(isExtensionNum(telnum)){
            return false;
        }
        if(isHotNum(telnum)){
            return false;
        }
        if(telnum.length() >= 11 && telnum.length() <= 12){
            if((telnum.startsWith("0") || telnum.startsWith("1")) && !telnum.startsWith("100") ){
                return true;
            }
        }
        return false;
    }
}
