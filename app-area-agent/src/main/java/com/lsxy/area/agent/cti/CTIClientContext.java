package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.commander.Client;
import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

import static javax.xml.bind.JAXBIntrospector.getValue;

/**
 * Created by tandy on 16/8/5.
 * cti 客户端  环境 用来管理多个客户端连接
 */
@Component
public class CTIClientContext {
    private static final Logger logger = LoggerFactory.getLogger(CTIClientContext.class);
    public ListOrderedMap clients = new ListOrderedMap();


    public void add(Byte clientid,Client client) {
        clients.put(clientid,client);
    }

    /**
     * 获取一个有效的CTI客户端连接对象进行操作
     * 需要考虑:CTI负载情况  会话相关(会议成员需要被分配到同一个CTI服务)
     *
     * @return
     */
    public Client getAvalibleClient() {
        //TODO 选择CTI客户端的规则
        Client client = null;
        try{

            client = (Client) clients.getValue(0);
        }catch(Exception ex){
            logger.error("没有找到一个有效的CTI客户端");
        }
        return client;
    }
}
