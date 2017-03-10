package com.lsxy.msg.supplier.common;

import com.lsxy.yunhuni.api.product.enums.ProductCode;

/**
 * Created by liups on 2017/3/8.
 */
public class MsgConstant {
    public static final int MaxMsMLength = 70;//普通短信长度扣费
    public static final int MaxOneMsMLength = 67;//普通短信长度扣费
    public static final int MaxUssdLength = 62;//普通短信长度扣费
    public static final int MaxNum = 10000;//目前最大支持10000个群发
    public static final String NumRegexStr = ",";//多手机号码用逗号分割
    public static final String ParamRegexStr = ";";//多参数用分号分割
    public static final String REPLACE_SYMBOL = "#*#";//占位符

    public static final String OTHER_ERROR_CODE = "-1";
    public static final String SUCCESS = "0";
    public static final String FAIL = "-1";
    public  static final String MSG_USSD = ProductCode.msg_ussd.name();//模板闪印
    public static final String MSG_SMS = ProductCode.msg_sms.name();//普通模板短信
    public static final String ChinaMobile  = "中国移动";//"中国移动";
    public static final String ChinaUnicom = "中国联通";//"中国联通";
    public static final String ChinaTelecom = "中国电信";//"中国电信";

    public static String TimePartten =  "yyyy-MM-dd HH:mm:ss";//2017-02-13 18:45:10

    public static String AwaitingTaskId = "mqAwaiting"; //当taskId为这个时，表示在延迟队列里待发送

    public static final int SEND_FIAL_MAX_NUM = 3;
}
