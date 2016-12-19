package com.lsxy.area.server.util;

import com.lsxy.framework.core.utils.UUIDGenerator;

import java.util.Date;

/**
 * Created by liuws on 2016/11/26.
 */
public class RecordFileUtil {

    private static final String FORMAT = "/data/prd/r/%s/%s/%s/%s/%s/%s.wav";

    public static String getRecordFileUrl(String tenantId,String appId){
        Date cur = new Date();
        String year = ""+ (1900 +cur.getYear());
        String month = ""+ (1 + cur.getMonth());
        String day = "" + cur.getDate();
        return String.format(FORMAT,tenantId,appId,year,month,day, UUIDGenerator.uuid());
    }
}
