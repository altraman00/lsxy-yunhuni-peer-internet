package com.lsxy.area.agent.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.config.SystemConfig;
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
import java.util.Map;

/**
 * Created by zhangxb on 2016/9/12.
 */
@Component
public class Handler_MN_CH_VF_DELETED extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_VF_DELETED.class);
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
        return ServiceConstants.MN_CH_VF_DELETED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应MN_CH_VF_DELETED:{}",request);
        }
        String jsonList = request.getBodyAsString();
        Map<String,Object> map = JSON.parseObject(jsonList, Map.class);
        String type = (String)map.get("type");
        boolean flag = false;
        if("file".equals(type)){
            //删除文件
            flag = deleteFile((String)map.get("filePath"));
        }else if("app".equals(type)){
            //删除文件夹
            flag = deleteDirectory((String)map.get("filePath"));
        }
        map.put("flag",flag);
        if(logger.isDebugEnabled()){
            logger.debug("删除放音文件结果:{}",flag);
        }
        String param = JSON.toJSON(map).toString();
        RPCRequest request1 = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_DELETED_OK,"");
        request1.setBody(param);
        try {
            if(logger.isDebugEnabled()) {
                logger.debug("发送删除放音文件成功指令成功:");
            }
            rpcCaller.invoke(sessionContext,request1,true);
        } catch (Exception ex) {
            logger.error("发送删除放音文件成功指令失败:"+request1,ex);
        }
        return null;
    }
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }else if(!file.exists()){//文件不存在也成功
            flag = true;
        }
        return flag;
    }
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    private  boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
