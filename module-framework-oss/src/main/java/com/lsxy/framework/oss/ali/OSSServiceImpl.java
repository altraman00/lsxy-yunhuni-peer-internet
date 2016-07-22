package com.lsxy.framework.oss.ali;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.lsxy.framework.oss.OSSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tandy on 2016/7/14.
 * 阿里云OSS 云存储方案实现
 * repository ==> oss bucket
 *
 */
@Service
@Lazy(true)
public class OSSServiceImpl implements OSSService{

    public static final Logger logger = LoggerFactory.getLogger(OSSServiceImpl.class);

    @Autowired
    private AliOSSClientFactoryBean afb;

    @Override
    public boolean uploadFileStream(InputStream inputStream, long fileLength, String orignalFileName, String repository, String fileKey) {
        long starttime = System.currentTimeMillis();
        try {
            OSSClient client = afb.getObject();
            client.putObject(repository, fileKey, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        long endtime = System.currentTimeMillis();
        if (logger.isDebugEnabled()){
                logger.debug("upload file {}({}) total spend {} ms",orignalFileName,fileKey,(endtime-starttime));
         }
        return true;
    }

    @Override
    public boolean uploadFileLocal(File file, String repository,  String fileKey) {
        try {
            OSSClient client = afb.getObject();
            InputStream inputStream = new FileInputStream(file);
            client.putObject(repository, fileKey, inputStream);
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public InputStream getFileStream(String repository, String fileKey) throws Exception {
        OSSClient client = afb.getObject();
        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(repository, fileKey);

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();
        return objectContent;
    }

    @Override
    public File downLoadFile(String repository, String fileKey, String destFile) throws Exception {
        OSSClient ossClient = this.afb.getObject();
        File file = new File(destFile);
        ossClient.getObject(new GetObjectRequest(repository, fileKey), file);
        return file;
    }

    @Override
    public String getFilePublicUrl(String repository,  String fileKey) {
        return null;
    }

    @Override
    public List<String> listObjects(String repository, String path) throws Exception {
        // 初始化 OSSClient
        OSSClient client = afb.getObject();
        // 构造 ListObjectsRequest 请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(repository);
        // 设置参数
        listObjectsRequest.setPrefix(path);
        ObjectListing listing = client.listObjects(listObjectsRequest);

        List<String> keysLst = new ArrayList<String>();
        // 遍历所有 Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            keysLst.add(objectSummary.getKey());
        }

        return keysLst;
    }

    @Override
    public void deleteObject(String repository, String path) throws UnsupportedOperationException {
        try {
            // 初始化 OSSClient
            OSSClient client = afb.getObject();
            client.deleteObject(repository,path);
        }catch (Exception ex){
            throw new UnsupportedOperationException();
        }
    }


}
