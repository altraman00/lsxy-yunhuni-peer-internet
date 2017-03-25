package com.msg.jimu;

/**
 * 积木通讯
 * Created by zhangxb on 2017/3/24.
 */
public class JiMuClient {
    //应用ID
    private static final String APP_ID = "b305886d5a674ea19cb89b540848c2fd";
    //应用私钥
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK8TJRLSjCfcoOsJ3HvMHn0yq2DQ3+TZYZOIzb2+TKEGEpdfX6aAxMxXJoIcVvYvD9fsGjrh8Nyiji4KRBO9s2c3KcWRrE3LcY1qPzSfWDKpBC/Vh8aGMGJcamaQZ0CNjRpZWa9ZGj0gqryZLbieqPb/Qd8ADupps8bWUr4Zc55zAgMBAAECgYAi3/5vnG989U884s7//i5IpFJNS+59yOvOFt9dH85l+S605rPIGDibt/Sc0IZmLgPK8GZdOmrfmKYX98WN5SkaXyAFfURJmMfY+lLR83h4on9Y++0iFglaczsuqaLQl2ng9rapmboEjI6uIu9S1Eu1VD5dsyGVl2KXcQZtI8xDYQJBANeCXJPIGjjcjbA5hHqlKsWXFOO5ct65ubg7R6RfUzcnLlMeXnbT2W1R0werVBWW/um2lIVoi0bVOpFybbOm4lECQQDP9/QJNduBdFCcobvdMgTUdvCQOkGhPR2KcbdZV/zTC6cbAV066qvP/fhhiS6jBb1+DFxaYw7lNsYVvOp7xh+DAkEAsLdpCpWPSwkv9k1z4M4HYLceng23sAND01poyNSR/CvYxLemZnIZXJc4IJXRAVMr/KGG8E5lxtOajyHx6trxwQJAC76P7Hk7JSthZgcIKeyqMyloWmy/yhA+e9QCK6x5XZ252R+RmUMpYryP60oo9RHc6qJeEMipTa5zL0UU5NqCawJBAJB8v47yU9wHFfltueMKk6BXNIqKuWJIBTc+R/LArPTyeagnGZwvUZBskCU5dVhAcZsPnidcPh6leYYlJi2ImTM= > >";
    //应用公钥
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvEyUS0own3KDrCdx7zB59Mqtg0N/k2WGTiM29vkyhBhKXX1+mgMTMVyaCHFb2Lw/X7Bo64fDcoo4uCkQTvbNnNynFkaxNy3GNaj80n1gyqQQv1YfGhjBiXGpmkGdAjY0aWVmvWRo9IKq8mS24nqj2/0HfAA7qabPG1lK+GXOecwIDAQAB";

    //单发
    private static final String SEND_SINGLE_SMS = "http://117.71.38.58:7080/jimu-run/camel/sms/sendSingleSMS?app_id=6f5a41e4c7c848a2b6637e30d6c10960&req_time=20161011181716&sign=";
    //查询单发结果
    private static final String get_single_result = "http://117.71.38.58:7080/jimu-run/camel/sms/getReport?app_id=123&ReqTime= 20161031121212";
    //查询多结果
    private static final String get_all_result = "http://117.71.38.58:7080/jimu-run/camel/sms/getReportV2?app_id=123&ReqTime= 20161031121212";

    /*MSMSID	请求流水号	String	32	否
SMSText	短信内容	String	140	否
ObjMobile	短信目标	String	11	否
    * */
    public static String send(){

    }
}
