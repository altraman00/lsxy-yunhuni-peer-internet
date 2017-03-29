package com.msg.jimu;

import com.msg.paopaoyu.PaoPaoYuClient;
import com.ustcinfo.ishare.eip.CallJiMu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 积木通讯
 * Created by zhangxb on 2017/3/24.
 */
public class JiMuClient {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuClient.class);
    private static final String ERROR_STR = "{\"ResultDesc\":\"系统错误\",\"ResultCode\":\"10007\"}";
    private static final String partten = "yyyyMMddHHmmss";
    //应用ID
    private static final String APP_ID = "b305886d5a674ea19cb89b540848c2fd";
    //应用私钥
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK8TJRLSjCfcoOsJ3HvMHn0yq2DQ3+TZYZOIzb2+TKEGEpdfX6aAxMxXJoIcVvYvD9fsGjrh8Nyiji4KRBO9s2c3KcWRrE3LcY1qPzSfWDKpBC/Vh8aGMGJcamaQZ0CNjRpZWa9ZGj0gqryZLbieqPb/Qd8ADupps8bWUr4Zc55zAgMBAAECgYAi3/5vnG989U884s7//i5IpFJNS+59yOvOFt9dH85l+S605rPIGDibt/Sc0IZmLgPK8GZdOmrfmKYX98WN5SkaXyAFfURJmMfY+lLR83h4on9Y++0iFglaczsuqaLQl2ng9rapmboEjI6uIu9S1Eu1VD5dsyGVl2KXcQZtI8xDYQJBANeCXJPIGjjcjbA5hHqlKsWXFOO5ct65ubg7R6RfUzcnLlMeXnbT2W1R0werVBWW/um2lIVoi0bVOpFybbOm4lECQQDP9/QJNduBdFCcobvdMgTUdvCQOkGhPR2KcbdZV/zTC6cbAV066qvP/fhhiS6jBb1+DFxaYw7lNsYVvOp7xh+DAkEAsLdpCpWPSwkv9k1z4M4HYLceng23sAND01poyNSR/CvYxLemZnIZXJc4IJXRAVMr/KGG8E5lxtOajyHx6trxwQJAC76P7Hk7JSthZgcIKeyqMyloWmy/yhA+e9QCK6x5XZ252R+RmUMpYryP60oo9RHc6qJeEMipTa5zL0UU5NqCawJBAJB8v47yU9wHFfltueMKk6BXNIqKuWJIBTc+R/LArPTyeagnGZwvUZBskCU5dVhAcZsPnidcPh6leYYlJi2ImTM= > >";
    //应用公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvEyUS0own3KDrCdx7zB59Mqtg0N/k2WGTiM29vkyhBhKXX1+mgMTMVyaCHFb2Lw/X7Bo64fDcoo4uCkQTvbNnNynFkaxNy3GNaj80n1gyqQQv1YfGhjBiXGpmkGdAjY0aWVmvWRo9IKq8mS24nqj2/0HfAA7qabPG1lK+GXOecwIDAQAB";
    private static final String SERVE_IP = "117.71.38.58";
    private static final String SERVE_PORT = "7080";
    //单发
    private static final String SEND_SINGLE_SMS = "/jimu-run/camel/sms/sendSingleSMS";
    //查询单发结果
    private static final String GET_SINGLE_RESULT = "/jimu-run/camel/sms/getReport";
    //查询多结果
    private static final String GET_ALL_RESULT = "/jimu-run/camel/sms/getReportV2?app_id=123&ReqTime= 20161031121212";

    /**新建一个实例 直接传参数 ip、 端口号**/
    private static final CallJiMu call = new CallJiMu(SERVE_IP,SERVE_PORT);

    /**
     * 单发
     * @param msgKey 标志Key
     * @param content 发送内容
     * @param mobile 手机号码
     * @return
     */
    public String send(String msgKey,String content,String mobile){
        String body = "{\"MSMSID\":\""+msgKey+"\",\"SMSText\":\""+content+"\",\"ObjMobile\":\""+mobile+"\"}";
        logger.info(body);
        try {
            String result = call.Call(APP_ID, PRIVATE_KEY, body, "1");
            return result;
        } catch (Exception e) {
            logger.info("电信积木调用异常",e);
        }
        return ERROR_STR;
    }

    /**
     * 查询单条记录
     * @param msgKey 主记录标识
     * @param taskId 任务ID
     * @return
     */
    public String getSignResult(String taskId,String msgKey){
        String body = "{\"MSMSID\":\""+msgKey+"\",\"SMSID\":\""+taskId+"\"}";
        logger.info(body);
        try {
            String result = call.Call(APP_ID, PRIVATE_KEY, body, "5");
            return result;
        } catch (Exception e) {
            logger.info("电信积木调用异常",e);
        }
        return ERROR_STR;
    }

    public static void main(String[] args) {
        //String str = send(UUIDGenerator.uuid(),"您的验证码为123456,5分钟内有效。","15360059775",new Date());
        //System.out.println(str);
        //{"ResultCode":"0","ResultDesc":"成功","ServerTime":"2017-03-27 14:40:23","MSMSID":"31adb1e15847566413536039bda81493","SMSID":"2f309cd1e71e4a13a05359b8d29e609f"}
        JiMuClient jiMuClient = new JiMuClient();
        String str1 = jiMuClient.getSignResult("c069c4f068b003a8fdd049715fd11f8c","0aa8427149084f06b3c5b3c79601b436");
        System.out.println(str1);
        //{"ResultCode":"0","ResultDesc":"成功","ServerTime":"2017-03-27 14:42:07","StatusReportList":[{"SMSID":"2f309cd1e71e4a13a05359b8d29e609f","MSMSID":"31adb1e15847566413536039bda81493","ObjMobile":"15360059775","GStatus":"DELIVRD"}]}
    }

}
