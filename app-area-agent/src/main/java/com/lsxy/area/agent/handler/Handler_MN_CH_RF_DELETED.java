package com.lsxy.area.agent.handler;

import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/9/12.
 */
@Component
public class Handler_MN_CH_RF_DELETED extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_RF_DELETED.class);
    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_RF_DELETED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应MN_CH_VF_DELETED:{}",request);
        }
        String jsonList = request.getBodyAsString();
        List<Map> list = JSON.parseArray(jsonList, Map.class);
        List<Map> rList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Map map = list.get(i);
            boolean flag = deleteFile((String)map.get("url"));
            map.put("flag",flag);
            rList.add(map);
        }
        String param = JSON.toJSON(rList).toString();
        if(logger.isDebugEnabled()){
            logger.debug("删除录音文件结果:{}",param);
        }
        RPCRequest request1 = RPCRequest.newRequest(ServiceConstants.MN_CH_RF_DELETED_OK,"");
        request1.setBody(param);
        try {
            logger.error("发送删除录音文件成功指令成功:");
            rpcCaller.invoke(sessionContext,request1,true);
        } catch (Exception ex) {
            logger.error("发送删除录音文件成功指令失败:"+request1,ex);
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
