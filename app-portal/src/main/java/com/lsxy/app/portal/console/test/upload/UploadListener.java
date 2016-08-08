package com.lsxy.app.portal.console.test.upload;

import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * 文件上传监听
 * Created by zhangxb on 2016/6/20.
 */
public class UploadListener implements ProgressListener {
    private static final Logger logger = LoggerFactory.getLogger(UploadListener.class);
    private HttpSession session;
    public UploadListener() {
    }
    public UploadListener(HttpSession _session) {
        session=_session;
        UploadEntity fuploadStatus = new UploadEntity();
        session.setAttribute("upload_ps", fuploadStatus);
    }
    public void update(long pBytesRead, long pContentLength, int pItems) {
        UploadEntity fuploadStatus = (UploadEntity) session.getAttribute("upload_ps");
        fuploadStatus.setUploadTotalSize(pContentLength);
        //读取完成
        if (pContentLength == -1) {
            fuploadStatus.setStatus("完成对" + pItems + "个文件的读取：读取了 " + pBytesRead + "/"  + pContentLength+ " bytes.");
            fuploadStatus.setReadTotalSize(pBytesRead);
            fuploadStatus.setCurrentUploadFileNum(pItems);
            fuploadStatus.setProcessEndTime(System.currentTimeMillis());
            fuploadStatus.setProcessRunningTime(fuploadStatus.getProcessEndTime());
        }else{//读取过程中
            fuploadStatus.setStatus("当前正在处理第" + pItems+"个文件:已经读取了 " + pBytesRead + " / " + pContentLength+ " bytes.");
            fuploadStatus.setReadTotalSize(pBytesRead);
            fuploadStatus.setpBytesRead(pBytesRead);
            fuploadStatus.setpContentLength(pContentLength);
            fuploadStatus.setCurrentUploadFileNum(pItems);
            fuploadStatus.setProcessRunningTime(System.currentTimeMillis());
        }
        logger.info("已经读取：" + fuploadStatus.getStatus());
        session.setAttribute("upload_ps", fuploadStatus);
    }
}
