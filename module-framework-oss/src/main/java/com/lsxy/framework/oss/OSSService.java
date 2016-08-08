package com.lsxy.framework.oss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Tandy on 2016/7/14.
 */
public interface OSSService {

    /**
     * 通过流方式上传文件到云存储空间
     * @param stream          输入流
     * @param fileLength    文件长度
     * @param orignalFileName      源文件名称
     * @return
     */
    public boolean uploadFileStream(InputStream stream, long fileLength,String orignalFileName,String repository,  String fileKey);

    /**
     * 上传本地文件到云存储空间
     * @param file
     * @param repository
     * @return
     */
    public boolean uploadFileLocal(File file,String repository, String fileKey) throws Exception;


    /**
     * 获取云存储文件流
     * @param repository
     * @return
     */
    public  InputStream getFileStream(String repository, String fileKey) throws Exception;

    /**
     * 下载文件到本地
     * @param repository
     * @param fileKey
     * @param destFile
     * @return
     */
    public File downLoadFile(String repository,String fileKey,String destFile) throws Exception;
    /**
     * 获取文件公共url
     * @param repository
     * @return
     */
    public String getFilePublicUrl(String repository, String fileKey);

    /**
     *  列出目录下的所有对象
     * @param repository
     * @param path
     * @return
     */
    public List<String> listObjects(String repository,String path) throws Exception;

    /**
     * 删除指定的文件对象
     * @param repository
     * @param path
     * @throws UnsupportedOperationException
     */
    public void deleteObject(String repository,String path) throws UnsupportedOperationException;

  /**
   * 上传表单提交的文件
   * @param tenantId
   * @param folder
   * @param file
   * @return
   * @throws IOException
   */
    public String uploadFile(String tenantId,String folder, MultipartFile file) throws IOException;
}
