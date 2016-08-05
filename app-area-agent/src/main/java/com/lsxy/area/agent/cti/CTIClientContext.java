package com.lsxy.area.agent.cti;

import com.lsxy.app.area.cti.commander.Client;
import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.stereotype.Component;

import java.util.Map;

import static javax.xml.bind.JAXBIntrospector.getValue;

/**
 * Created by tandy on 16/8/5.
 * cti 客户端  环境 用来管理多个客户端连接
 */
@Component
public class CTIClientContext {
    public ListOrderedMap clients = new ListOrderedMap();


    public void add(Byte clientid,Client client) {
        clients.put(clientid,client);
    }

    public Client getAvalibleClient() {
        Client client = (Client) clients.getValue(0);
        return client;
    }
}
