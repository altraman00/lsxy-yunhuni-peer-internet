package com.lsxy.app.api.gateway.rest.record.vo;

/**
 * Created by liups on 2017/3/29.
 */
public class DownloadUrlVo {
    public static final int STATE_WAIT = 0;
    public static final int STATE_DONE = 1;
    private int state;
    private String url;

    public DownloadUrlVo() {
    }

    public DownloadUrlVo(int state, String url) {
        this.state = state;
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
