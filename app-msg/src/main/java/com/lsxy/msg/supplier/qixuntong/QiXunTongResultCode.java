package com.lsxy.msg.supplier.qixuntong;

/**
 * 企讯通错误码
 * Created by zhangxb on 2017/1/10.
 */
public enum QiXunTongResultCode {
    QiXunTong_ERROR_10001("1","JSON的格式错误"),
    QiXunTong_ERROR_10002("2","缺少cpid参数"),
    QiXunTong_ERROR_10003("3","渠道状态异常"),
    QiXunTong_ERROR_10004("4","缺少topid参数"),
    QiXunTong_ERROR_10005("5","渠道能力异常"),
    QiXunTong_ERROR_10006("6","缺少serviceid参数"),
    QiXunTong_ERROR_10007("7","渠道业务异常"),
    QiXunTong_ERROR_10008("8","缺少msg参数"),
    QiXunTong_ERROR_10009("9","msg长度非法"),
    QiXunTong_ERROR_100010("10","包含敏感词"),
    QiXunTong_ERROR_200011("11","缺少mobiles参数"),
    QiXunTong_ERROR_200012("12","mobiles长度非法"),
    QiXunTong_ERROR_200013("13","缺少excode参数"),
    QiXunTong_ERROR_200014("14","缺少templetid参数"),
    QiXunTong_ERROR_200015("15","缺少sign参数"),
    QiXunTong_ERROR_200016("16","签名校验不通过"),
    QiXunTong_ERROR_200017("17","程序异常")
    ;

    private String resultCode;
    private String resultDesc;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    QiXunTongResultCode(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
    public static String getQiXunTongResult(String resultCode, String resultDesc){
        for (QiXunTongResultCode s : QiXunTongResultCode.values()){
            if(s.getResultCode().equals(resultCode)){
                return s.getResultDesc();
            }
        }
        return resultDesc;
    }
}
