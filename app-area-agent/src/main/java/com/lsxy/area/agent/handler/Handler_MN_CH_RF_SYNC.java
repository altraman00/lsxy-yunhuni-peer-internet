package com.lsxy.area.agent.handler;


import com.alibaba.fastjson.JSON;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.core.utils.ZipUtil;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;


/**
 * Created by zhangxb on 2016/09/05.
 */
@Component
public class Handler_MN_CH_RF_SYNC extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_RF_SYNC.class);
    //文件保存地址 area.agent.file.play "/data/prd/p/0"
    private static final String path = SystemConfig.getProperty("area.agent.file.play");
    private static String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
    @Autowired
    private OSSService ossService ;
    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_RF_SYNC;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应RF SYNC:{}",request);
        }
        //获取参数
        String jsonList = request.getBodyAsString();
        //转换成map对象
        Map<String,Object> map = JSON.parseObject(jsonList, Map.class);
        if(map!=null){
            List<String> list = (List<String>)map.get("files");
            //合并多文件，产生临时文件，将临时文件上传
            //验证文件是否存在
            boolean isFile = false;
            for(int i=0;i<list.size();i++) {
                File file = new File(list.get(i));
                if (!file.exists()) {
                    isFile = true;
                    if(logger.isDebugEnabled()){
                        logger.debug("同步录音文件失败,原因:[{}]不存在",list.get(i));
                    }
                    break;
                }
            }
            if(isFile){
                //文件不存在
                map.put("status", -2);
            }else {
                //临时文件名
                String fileName = UUIDGenerator.uuid() + ".zip";
                //获取在Oss中的标识 生成规则play_voice/tenant_id/app_id/yyyyMMdd/uuid
                String ymd = DateUtils.formatDate(new Date(), "yyyyMMdd");
                String ossUri = "tenant_res/" + map.get("tenantId") + "/record_voice/" + map.get("appId") + "/" + ymd + "/" + fileName;
                String tempUri = "/tmp/" + fileName;
                logger.info("打包文件地址:["+tempUri+"]");
                //进行压缩
                ZipUtil.zip(list, tempUri);
                //上传文件
                File file = new File(tempUri);
                boolean flag = true;
                try {
                    //设置为流格式
                    flag = ossService.uploadFileLocal(file, repository, ossUri, "application/octet-stream", fileName);
                } catch (Exception e) {
                    flag = false;
                    logger.error("录音文件同步失败：" + e);
                }
                int status = flag?1:-1;
                map.put("status", status);
                map.put("ossUri", ossUri);
                //删除压缩文件
                if(file.exists()) {
                    if(logger.isDebugEnabled()){
                        logger.debug("删除录音上传临时文件:{}",tempUri);
                    }
                    file.delete();
                }
            }
        }
        //回调通知
        String param = JSON.toJSON(map).toString();
        if(logger.isDebugEnabled()){
            logger.debug("录音同步结果:{}",param);
        }
        RPCRequest request1 = RPCRequest.newRequest(ServiceConstants.MN_CH_RF_SYNC_OK,"");
        request1.setBody(param);
        try {
            rpcCaller.invoke(sessionContext,request1,true);
        } catch (Exception ex) {
            logger.error("录音放音文件指令失败:"+request1,ex);
        }
        return null;
    }
}
