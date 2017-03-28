package com.lsxy.msg.supplier.jimu;

import com.lsxy.msg.supplier.AbstractSupplierSendServiceImpl;
import com.lsxy.msg.supplier.common.ResultOne;
import com.lsxy.msg.supplier.paopaoyu.PaoPaoYuMassNofity;
import com.msg.jimu.JiMuClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
@Service("jiMuService")
public class JiMuServiceImpl extends AbstractSupplierSendServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(JiMuServiceImpl.class);
    private JiMuClient jiMuClient;

    public JiMuClient getJiMuClient(){
        if(jiMuClient == null){
            synchronized(JiMuServiceImpl.class){
                if(jiMuClient == null){
                    jiMuClient = new JiMuClient();
                }
            }
        }
        return jiMuClient;
    }

    @Override
    public ResultOne sendOne(String tempId, List<String> tempArgs, String msg, String mobile, String sendType, String msgKey) {
        String result = getJiMuClient().send(msgKey,msg,mobile);
        logger.info("调用[积木通讯][单发][模板短信]结果:"+ result);
        return new JiMuResultOne(result);
    }
    @Override
    public Object getTask(String taskId,Object ...params) {
        String result = getJiMuClient().getSignResult(taskId,(String)params[0]);
        logger.info("调用[积木通讯][查询单发]结果:"+ result);
        return JiMuResultOne.getRequestResult(result,taskId);
    }
}
