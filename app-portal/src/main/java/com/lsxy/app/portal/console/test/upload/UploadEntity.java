package com.lsxy.app.portal.console.test.upload;

import java.io.Serializable;

/**
 * 一次文件上传对象
 * Created by zhangxb on 2016/6/20.
 */
public class UploadEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int pCountItems=0;//总文件数量
    private int pItems=0;//正在读取文件数
    private long pBytesRead=0L;//已读取文件内容
    private long pContentLength=0L;//文件大小
    private int ossCountItems=0;//需要保存到oss的文件数
    private int ossItems=0;//已保存到oss上的文件数
    private boolean flag = true;//是否异常
    private String msg = "";//保存信息

    public UploadEntity() {
    }

    public UploadEntity(int pCountItems) {
        this.pCountItems = pCountItems;
    }

    public int getpCountItems() {
        return pCountItems;
    }

    public int getOssCountItems() {
        return ossCountItems;
    }

    public void setOssCountItems(int ossCountItems) {
        this.ossCountItems = ossCountItems;
    }

    public void setpCountItems(int pCountItems) {
        this.pCountItems = pCountItems;
    }

    public int getpItems() {
        return pItems;
    }

    public void setpItems(int pItems) {
        this.pItems = pItems;
    }

    public long getpBytesRead() {
        return pBytesRead;
    }

    public void setpBytesRead(long pBytesRead) {
        this.pBytesRead = pBytesRead;
    }

    public long getpContentLength() {
        return pContentLength;
    }

    public void setpContentLength(long pContentLength) {
        this.pContentLength = pContentLength;
    }

    public int getOssItems() {
        return ossItems;
    }

    public void setOssItems(int ossItems) {
        this.ossItems = ossItems;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
