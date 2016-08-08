package com.lsxy.app.portal.console.test.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zhangxb on 2016/6/20.
 */
public class UploadCommonsMultipartResolver extends CommonsMultipartResolver {
    @SuppressWarnings("unchecked")
    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = "utf-8";
        FileUpload fileUpload = prepareFileUpload(encoding);
        HttpSession session = request.getSession();
        UploadListener uploadProgressListener = new UploadListener(session);
        fileUpload.setProgressListener(uploadProgressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        }
        catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        }
        catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }
}
