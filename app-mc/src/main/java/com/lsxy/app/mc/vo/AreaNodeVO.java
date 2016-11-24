package com.lsxy.app.mc.vo;

/**
 * Created by tandy on 16/11/24.
 */
public class AreaNodeVO {
    public static final String STATUS_OK = "OK";
    public static final String STATUS_FAILED = "FAILED";
    private String nodeId;
    private String host;
    private String status = STATUS_FAILED;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AreaNodeVO(String nodeId){}

    public AreaNodeVO(){}

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
