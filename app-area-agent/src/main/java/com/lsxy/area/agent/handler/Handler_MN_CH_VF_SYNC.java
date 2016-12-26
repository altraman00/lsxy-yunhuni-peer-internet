package com.lsxy.area.agent.handler;


import com.alibaba.fastjson.JSON;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangxb on 2016/09/05.
 */
@Component
public class Handler_MN_CH_VF_SYNC extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_VF_SYNC.class);
    //文件保存地址 area.agent.file.play "/data/prd/p/0"
    private static final String path = SystemConfig.getProperty("area.agent.file.play");
    @Autowired
    private OSSService ossService ;
    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_VF_SYNC;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应VF SYNC:{}",request);
        }
        String jsonList = request.getBodyAsString();
//        String jsonList = (String) request.getParameter(ServiceConstants.MN_CH_VF_SYNC);
        List<Map> list = JSON.parseArray(jsonList, Map.class);
        List<Map> rList = new ArrayList<Map>();
        for(int i=0;i<list.size();i++){
            Map<String,String> vfp = list.get(i);
            //后缀名同意一小写
            String type = vfp.get("name").substring(vfp.get("name").lastIndexOf("."), vfp.get("name").length()).toLowerCase();
            String saveFilePath = path+"/"+vfp.get("tenantId")+"/"+vfp.get("appId")+"/"+vfp.get("id")+type;
            Integer result = downFile(vfp.get("fileKey"),saveFilePath);
            vfp.put("sync",result+"");
            rList.add(vfp);
        }
        String param = JSON.toJSON(rList).toString();
        if(logger.isDebugEnabled()){
            logger.debug("同步结果C:{}",param);
        }
        RPCRequest request1 = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_SYNC_OK,"");
        request1.setBody(param);
        try {
            rpcCaller.invoke(sessionContext,request1,true);
        } catch (Exception ex) {
            logger.error("发送放音文件指令失败:"+request1,ex);
        }
        return null;
    }
    /**
     * 下载文件
     * @param uri 文件对于的oss地址
     * @param path 保存文件地址
     * @return
     */
    private Integer downFile(String uri,String path){
        Integer result = -1;
        logger.error("文件下载开始{}---->{},时间:{}",uri,path, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        File newFile = new File(path);
        long start = new Date().getTime();
        //先判断文件是否存在，如果存在则不下载
        if(newFile.exists()){
            logger.info("文件已存在，覆盖原文件:{},下载信息:{}",path,uri);
        }
        //补充文件夹
        new File(path.substring(0,path.lastIndexOf("/"))).mkdirs();
        //开始写文件
        try {
            ossService.downLoadFile(SystemConfig.getProperty("global.oss.aliyun.bucket"), uri,path);
            result = 1;
        } catch (Exception e) {
            logger.error("文件流输出异常,{]", e);
        }
        logger.error("文件下载结束{}---->{},结果:{},时间:{},花费时间{}",uri,path,result, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),new Date().getTime()-start);
        return result;
    }
//    /**
//     * 下载文件
//     * @param uri 文件对于的oss地址
//     * @param path 保存文件地址
//     * @return
//     */
//    private Integer downFile(String uri,String path){
//        Integer result = -1;
//        logger.error("文件下载开始{}---->{},时间:{}",uri,path, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
//        File newFile = new File(path);
//        long start = new Date().getTime();
//        //先判断文件是否存在，如果存在则不下载
//        if(newFile.exists()){
//            logger.info("文件已存在，覆盖原文件:{},下载信息:{}",path,uri);
//        }
//        //补充文件夹
//        new File(path.substring(0,path.lastIndexOf("/"))).mkdirs();
//        //开始写文件
//        InputStream in = null;
//        FileOutputStream out = null;
//        try {
//            in = ossService.getFileStream(SystemConfig.getProperty("global.oss.aliyun.bucket"), uri);
//            out = new FileOutputStream(newFile);
//            byte[] buffer = new byte[8 * 1024];
//            int length;
//            while ((length = in.read(buffer)) > 0) {
//                out.write(buffer, 0, length);
//            }
//            result = 1;
//        } catch (Exception e) {
//            logger.error("文件流输出异常,{]", e);
//        }finally {
//            try {
//                if(in!=null) {
//                    in.close();
//                }
//                if(out!=null) {
//                    out.close();
//                }
//            } catch (Exception e) {
//                logger.error("文件流关闭异常，{}", e);
//            }
//        }
//        logger.error("文件下载结束{}---->{},结果:{},时间:{},花费时间{}",uri,path,result, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),new Date().getTime()-start);
//        return result;
//    }
}
