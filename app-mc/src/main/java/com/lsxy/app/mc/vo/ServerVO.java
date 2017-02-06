package com.lsxy.app.mc.vo;

/**
 * Created by tandy on 16/11/21.
 */
public class ServerVO {
    public static final String STATUS_OK = "正常" ;           // 服务正常
    public static final String STATUS_FAILED = "启动失败";     //服务启动失败
    public static final String STATUS_STOPED = "已停止";    //服务停止了
    public static final String STATUS_STARTING = "启动中";    //服务启动中
    public static final String STATUS_UPDATING = "更新中";
    private String serverName;
    private String appName;     //systemId
    private String moduleName;  //模块名称
    private String serverHost;
    private String serverIp;
    private String serverPort;
    private String status = STATUS_STOPED;
    private String version;
    private String startDt;


    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ServerVO(){}

    public ServerVO(String serverName, String appName, String hostName, String serverIp, String serverPort, String status) {
        this.serverName = serverName;
        this.appName = appName;
        this.serverHost = hostName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.status = status;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
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
