package com.lsxy.framework.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Tandy on 2016/7/14.
 */
public interface OSSService {

    /**
     * 通过流方式上传文件到云存储空间
     * @param stream          输入流
     * @param fileLength    文件长度
     * @param orignalFileName      源文件名称
     * @param  destFileName  目标文件名称
     * @param path              云存储的路径
     * @return
     */
    public String uploadFileStream(InputStream stream, long fileLength,String orignalFileName,String repository, String destFileName,String path);

    /**
     * 上传本地文件到云存储空间
     * @param file
     * @param repository
     * @param path
     * @param destFileName
     * @return
     */
    public String uploadFileLocal(File file,String repository,String path,String destFileName);


    /**
     * 获取云存储文件流
     * @param repository
     * @param path
     * @param fileName
     * @return
     */
    public  InputStream getFileStream(String repository,String path,String fileName);

    /**
     * 获取文件公共url
     * @param repository
     * @param path
     * @param fileName
     * @return
     */
    public String getFilePublicUrl(String repository,String path,String fileName);

}
