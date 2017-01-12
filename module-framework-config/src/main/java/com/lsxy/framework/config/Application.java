package com.lsxy.framework.config;

/**
 * Created by tandy on 17/1/12.
 */
public enum Application {

    APP_PORTAL("app.portal", "app-portal"),
    APP_API_GATEWAY("api.gateway", "app-api-gateway"),
    APP_AREA_SERVER("area.server", "app-area-server"),
    APP_AREA_AGENT("area.agent", "app-area-agent"),
    APP_BACKEND("app.backend", "app-backend"),
    APP_CALL_CENTER("app.cc", "app-call-center"),
    APP_MC("app.mc", "app-mc"),
    APP_OC("oc.api", "app-oc"),
    APP_OPENSIPS_PROXY("app.opensips.proxy", "app-opensips-proxy"),
    APP_THIRD_JOIN_GATEWAY("app.third.join.gateway", "app-third-join-gateway"),
    APP_PORTAL_API("portal.api", "app-portal-api");

    private String appName;
    private String moduleName;

    Application(String appName, String moduleName) {
        this.appName = appName;
        this.moduleName = moduleName;
    }

    public static Application getApplication(String appName) {
        if (appName.equals(APP_PORTAL.appName)) {
            return APP_PORTAL;
        }
        if (appName.equals(APP_API_GATEWAY.appName)) {
            return APP_API_GATEWAY;
        }
        if (appName.equals(APP_AREA_SERVER.appName)) {
            return APP_AREA_SERVER;
        }
        if (appName.equals(APP_AREA_AGENT.appName)) {
            return APP_AREA_AGENT;
        }
        if (appName.equals(APP_BACKEND.appName)) {
            return APP_BACKEND;
        }
        if (appName.equals(APP_CALL_CENTER.appName)) {
            return APP_CALL_CENTER;
        }
        if (appName.equals(APP_MC.appName)) {
            return APP_MC;
        }
        if (appName.equals(APP_OC.appName)) {
            return APP_OC;
        }
        if (appName.equals(APP_OPENSIPS_PROXY.appName)) {
            return APP_OPENSIPS_PROXY;
        }
        if (appName.equals(APP_THIRD_JOIN_GATEWAY.appName)) {
            return APP_THIRD_JOIN_GATEWAY;
        }
        if (appName.equals(APP_PORTAL_API.appName)) {
            return APP_PORTAL_API;
        }
        return null;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
