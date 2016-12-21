package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.Commander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/5.
 * cti 客户端  环境 用来管理多个客户端连接
 */
@Component
public class CTIClientContext {
    private static final Logger logger = LoggerFactory.getLogger(CTIClientContext.class);
    public Map<String,Commander> clients = new HashMap<>();


    public void add(String clientid, Commander client) {
        clients.put(clientid, client);
    }

    /**
     * 获取一个有效的CTI客户端连接对象进行操作
     * 需要考虑:CTI负载情况  会话相关(会议成员需要被分配到同一个CTI服务)
     *
     * @return
     */
    public Commander getAvalibleClient() {
        //TODO 选择CTI客户端的规则
//        Commander client = null;
//        try {
//            client = (Commander) clients.getValue(0);
//        } catch (Exception ex) {
//            logger.error("没有找到一个有效的CTI客户端");
//        }
//        return client;
        return null;
    }

    public boolean isNotExist(String serverIp) {
        return this.clients.containsKey(serverIp);
    }

    public void remove(String ip) {
        clients.remove(ip);
    }
}
