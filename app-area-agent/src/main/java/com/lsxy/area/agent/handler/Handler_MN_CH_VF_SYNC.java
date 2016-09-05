package com.lsxy.area.agent.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlaySyncOkEvent;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangxb on 2016/09/05.
 */
@Component
public class Handler_MN_CH_VF_SYNC extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_VF_SYNC.class);
    //文件保存地址
    private static final String path = "/data/prd/p/0";
    @Autowired
    private OSSService ossService;
    @Autowired
    private MQService mqService;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_VF_SYNC;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应VF SYNC:{}",request);
        }
        String jsonList = (String) request.getParameter(ServiceConstants.MN_CH_VF_SYNC);
        List<VoiceFilePlay> list = JSON.parseArray(jsonList, VoiceFilePlay.class);
        List<VoiceFilePlay> rList = new ArrayList<VoiceFilePlay>();
        for(int i=0;i<list.size();i++){
            VoiceFilePlay vfp = list.get(i);
            String type = vfp.getName().substring(vfp.getName().lastIndexOf(".")+1, vfp.getName().length());
            String saveFilePath = path+"/"+vfp.getTenant().getId()+"/"+vfp.getApp().getId()+"/"+vfp.getId()+type;
            Integer result = downFile(vfp.getFileKey(),saveFilePath);
            vfp.setSync(result);
            rList.add(vfp);
        }
        VoiceFilePlaySyncOkEvent vfpse = new VoiceFilePlaySyncOkEvent(rList);
        mqService.publish(vfpse);
        return null;
    }

    /**
     * 下载文件
     * @param uri 文件对于的oss地址
     * @param path 保存文件地址
     * @return
     */
    private Integer downFile(String uri,String path){
        Integer result = VoiceFilePlay.SYNC_FAIL;
        logger.error("文件下载开始{}---->{},时间:{}",uri,path, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        File newFile = new File(path);
        long start = new Date().getTime();
        //先判断文件是否存在，如果存在则不下载
        if(newFile.exists()){
            logger.info("文件已存在不用下载，文件:{},下载信息:{}",path,uri);
            result = VoiceFilePlay.SYNC_SUCCESS;
        }else{
            //补充文件夹
            new File(path.substring(0,path.lastIndexOf("/"))).mkdirs();
            //开始写文件
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = ossService.getFileStream(SystemConfig.getProperty("global.oss.aliyun.bucket"), uri);
                out = new FileOutputStream(newFile);
                byte[] buffer = new byte[8 * 1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                result = VoiceFilePlay.SYNC_SUCCESS;
            } catch (Exception e) {
                logger.error("文件流输出异常,{]", e);
            }finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    logger.error("文件流关闭异常，{}", e);
                }
            }
        }
        logger.error("文件下载结束{}---->{},结果:{},时间:{},花费时间{}",uri,path,result, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),new Date().getTime()-start);
        return result;
    }
}
