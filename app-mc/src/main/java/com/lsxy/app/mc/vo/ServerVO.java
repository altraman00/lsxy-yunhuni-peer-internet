package com.lsxy.app.mc.vo;

/**
 * Created by tandy on 16/11/21.
 */
public class ServerVO {
    public static final String STATUS_OK = "OK" ;
    public static final String STATUS_FAILED = "FAILED";
    private String serverName;
    private String appName;
    private String serverHost;
    private String serverIp;
    private String serverPort;
    private String status;


    public ServerVO(){}

    public ServerVO(String serverName, String appName, String hostName, String serverIp, String serverPort, String status) {
        this.serverName = serverName;
        this.appName = appName;
        this.serverHost = hostName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.status = status;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
