package com.msg.jimu;

/**
 * Created by zhangxb on 2017/3/25.
 */
public enum JiMuResultCode {
    //{"ResultCode":"8011","ResultDesc":"签名认证失败","ServerTime":"2017-03-27 11:01:14","ReqSeq":"63230a08f27c225c6c1d7e24d16846cd","RespSeq":"d2ba12467ff04b28a72b5c23fd6b570a"}
    JIMU_CODE_0("0","成功"),
JIMU_CODE_8001("8001","余额不足"),
JIMU_CODE_8003("8003","时间戳格式错误（yyyy-MM-dd H24:mi:ss）"),
JIMU_CODE_8004("8004","IP 鉴权失败"),
JIMU_CODE_8005("8005","用户不存在"),
JIMU_CODE_8006("8006","商户或者其当前下属登录用户状态不正常"),
JIMU_CODE_8007("8007","用户无此模板使用权限或者模板未审核或失效"),
JIMU_CODE_8008("8008","模板约定的参数个数与SMSText 格式不符"),
JIMU_CODE_8009("8009","短信内容中包含敏感词"),
JIMU_CODE_8010("8010","短信内容不包含关键词"),
JIMU_CODE_8011("8011","签名失败"),
JIMU_CODE_8012("8012","目标号码未添加为测试号码"),
JIMU_CODE_8013("8013","无法获取登录信息"),
JIMU_CODE_8014("8014","能力禁用"),
JIMU_CODE_8015("8015","成功"),
JIMU_CODE_8016("8016","目标号码已加入黑名单"),
JIMU_CODE_8017("8017","今日支付总金额超过限制"),
JIMU_CODE_8018("8018","业务异常"),
JIMU_CODE_8019("8019","请求流水号为空"),
JIMU_CODE_8020("8020","参数错误，发送内容为空"),
JIMU_CODE_8021("8021","目标号码为空"),
JIMU_CODE_8022("8022","目标号码不是电信号码"),
JIMU_CODE_8023("8023","目标号码不是合法的三网号码"),
JIMU_CODE_8024("8024","访问终端类型为空"),
JIMU_CODE_8025("8025","订单号为空"),
JIMU_CODE_8026("8026","支付金额为空"),
JIMU_CODE_8027("8027","return_url为空"),
JIMU_CODE_8028("8028","支付类型为空"),
JIMU_CODE_8029("8029","支付号码为空"),
JIMU_CODE_8030("8030","参数错误，内容参数只能是数字"),
JIMU_CODE_8031("8031","今日体验次数已达到3次"),
JIMU_CODE_8032("8032","短信流水号为空"),
JIMU_CODE_8033("8033","短信流水号或应用不存在"),
JIMU_CODE_8034("8034","振铃时长只能为数字"),
JIMU_CODE_8998("8998","其它错误(处理异常)"),
    JIMU_CODE_8999("8999","数据库入库错误");
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

    JiMuResultCode(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
    public static String getJiMuResult(String resultCode, String resultDesc){
        for (JiMuResultCode s : JiMuResultCode.values()){
            if(s.getResultCode().equals(resultCode)){
                return s.getResultDesc();
            }
        }
        return resultDesc;
    }
}
