package com.lsxy.framework.rpc.api.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/7/30.
 * cluster://192.168.1.11:9999|192.168.1.11:8888
 */
public class ClientConfig {
    public static final String PROTOCOL_CLUSTER="cluster://";
    public static final String PROTOCOL_SINGLE="host://";


    private String url;

    private String clientId;

    public ClientConfig(String clientId,String url){

    }



}
