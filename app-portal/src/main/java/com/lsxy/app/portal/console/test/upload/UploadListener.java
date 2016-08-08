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
    private HttpSession session ;
    public UploadListener(HttpSession session) {
        this.session = session;
        UploadEntity uploadEntity = new UploadEntity();
        session.setAttribute("upload_ps",uploadEntity);
    }
    public void update(long pBytesRead, long pContentLength, int pItems) {
        UploadEntity uploadEntity = (UploadEntity) session.getAttribute("upload_ps");
        uploadEntity.setpBytesRead(pBytesRead);
        uploadEntity.setpContentLength(pContentLength);
        uploadEntity.setpItems(pItems);
        //读取完成
        if (pContentLength == -1) {
            logger.info("已经读取：完成对" + pItems + "个文件的读取：读取了 " + pBytesRead + "/"  + pContentLength+ " bytes.");
        }else{//读取过程中
            logger.info("已经读取：当前正在处理第" + pItems+"个文件:已经读取了 " + pBytesRead + " / " + pContentLength+ " bytes.");
        }
        session.setAttribute("upload_ps", uploadEntity);
    }
}
