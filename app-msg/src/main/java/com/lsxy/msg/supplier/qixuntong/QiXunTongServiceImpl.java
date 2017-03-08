package com.lsxy.msg.supplier.qixuntong;

import com.lsxy.msg.supplier.AbstractSupplierSendServiceImpl;
import com.lsxy.msg.supplier.model.ResultMass;
import com.lsxy.msg.supplier.model.ResultOne;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
@Service("qiXunTongService")
public class QiXunTongServiceImpl extends AbstractSupplierSendServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(QiXunTongServiceImpl.class);

    private QiXinTongClient qiXinTongClient;

    public QiXinTongClient getQiXinTongClient(){
        if(qiXinTongClient == null){
            synchronized(QiXunTongServiceImpl.class){
                if(qiXinTongClient == null){
                    qiXinTongClient = new QiXinTongClient();
                }
            }

        }
        return qiXinTongClient;
    }

    @Override
    public ResultOne smsSendOne(String tempId, List<String> tempArgs, String msg, String mobile) {
        String result = getQiXinTongClient().sendTempleMSM("",tempId, msg ,  mobile);
        logger.info("调用[企信通][单发][模板短信]结果:"+ result);
        return new QiXunTongResultOne(result);
    }

    @Override
    public ResultMass smsSendMass(String taskName, String tempId, List<String> tempArgs,String msg,  List<String> mobiles, Date sendTime) {
        if(mobiles == null || mobiles.size() == 0){
            //TODO 抛异常
        }
        //TODO 组成字符串
        String mobilesStr = StringUtils.join(mobiles, ",");

        String result = getQiXinTongClient().sendTempleMSM(taskName,tempId, msg ,  mobilesStr);
        logger.info("调用[企信通][群发][模板短信]结果:"+ result);
        return new QiXunTongResultMass(result,mobilesStr);
    }

    @Override
    public String getSupplierCode() {
        return QiXunTongConstant.QixuntongCode;
    }

    @Override
    public int getMaxSendNum() {
        return QiXunTongConstant.QiXunTongMaxNum;
    }

}
