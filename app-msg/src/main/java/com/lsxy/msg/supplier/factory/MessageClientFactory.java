package com.lsxy.msg.supplier.factory;

import com.lsxy.msg.supplier.factory.client.PaoPaoYuClient;
import com.lsxy.msg.supplier.factory.client.QiXinTongClient;
import org.springframework.stereotype.Component;

/**
 * Created by zhangxb on 2016/11/12.
 */
@Component
public class MessageClientFactory {
    public  static final String SENDTYPE_USSD = "0";//闪印
    public static final String SENDTYPE_MSM = "1";//普通短信
    public static final String ChinaTelecom = "中国电信";
    public static final String ChinaMobile  = "中国移动";
    public static final String ChinaUnicom = "中国联通";
    private PaoPaoYuClient paoPaoYuClient;

    private QiXinTongClient qiXinTongClient;

    public PaoPaoYuClient getPaoPaoYuClient(){
        if(paoPaoYuClient == null){
            paoPaoYuClient = new PaoPaoYuClient();
        }
        return paoPaoYuClient;
    }

    public QiXinTongClient getQiXinTongClient(){
        if(qiXinTongClient == null){
            qiXinTongClient = new QiXinTongClient();
        }
        return qiXinTongClient;
    }
}
