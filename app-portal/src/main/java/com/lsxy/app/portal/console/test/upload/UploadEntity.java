package com.lsxy.app.portal.console.test.upload;

import java.util.ArrayList;
import java.util.List;

/**
 * 一次文件上传对象
 * Created by zhangxb on 2016/6/20.
 */
public class UploadEntity {

    //上传总量
    private long uploadTotalSize=0;
    //读取上传总量
    private long readTotalSize=0;
    private boolean flag = false;//全部上传保存流程走完为true
    //当前上传文件号
    private int currentUploadFileNum=0;
    //成功读取上传文件数
    private int successUploadFileCount=0;
    //状态
    private String status="";
    //处理起始时间
    private long processStartTime=0l;
    //处理终止时间
    private long processEndTime=0l;
    //处理执行时间
    private long processRunningTime=0l;
    //上传文件URL列表
    private List uploadFileUrlList=new ArrayList();
    //取消上传
    private boolean cancel=false;
    //上传base目录
    private String baseDir="";

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getBaseDir() {
        return baseDir;
    }
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
    public boolean getCancel() {
        return cancel;
    }
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
    public List getUploadFileUrlList() {
        return uploadFileUrlList;
    }
    public void setUploadFileUrlList(List uploadFileUrlList) {
        this.uploadFileUrlList = uploadFileUrlList;
    }
    public long getProcessRunningTime() {
        return processRunningTime;
    }
    public void setProcessRunningTime(long processRunningTime) {
        this.processRunningTime = processRunningTime;
    }
    public long getProcessEndTime() {
        return processEndTime;
    }
    public void setProcessEndTime(long processEndTime) {
        this.processEndTime = processEndTime;
    }
    public long getProcessStartTime() {
        return processStartTime;
    }
    public void setProcessStartTime(long processStartTime) {
        this.processStartTime = processStartTime;
    }
    public long getReadTotalSize() {
        return readTotalSize;
    }
    public void setReadTotalSize(long readTotalSize) {
        this.readTotalSize = readTotalSize;
    }
    public int getSuccessUploadFileCount() {
        return successUploadFileCount;
    }
    public void setSuccessUploadFileCount(int successUploadFileCount) {
        this.successUploadFileCount = successUploadFileCount;
    }
    public int getCurrentUploadFileNum() {
        return currentUploadFileNum;
    }
    public void setCurrentUploadFileNum(int currentUploadFileNum) {
        this.currentUploadFileNum = currentUploadFileNum;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public long getUploadTotalSize() {
        return uploadTotalSize;
    }
    public void setUploadTotalSize(long uploadTotalSize) {
        this.uploadTotalSize = uploadTotalSize;
    }

    @Override
    public String toString() {
        return "UploadEntity{" +
                "uploadTotalSize=" + uploadTotalSize +
                ", readTotalSize=" + readTotalSize +
                ", currentUploadFileNum=" + currentUploadFileNum +
                ", successUploadFileCount=" + successUploadFileCount +
                ", status='" + status + '\'' +
                ", processStartTime=" + processStartTime +
                ", processEndTime=" + processEndTime +
                ", processRunningTime=" + processRunningTime +
                ", uploadFileUrlList=" + uploadFileUrlList +
                ", cancel=" + cancel +
                ", baseDir='" + baseDir + '\'' +
                '}';
    }
}
