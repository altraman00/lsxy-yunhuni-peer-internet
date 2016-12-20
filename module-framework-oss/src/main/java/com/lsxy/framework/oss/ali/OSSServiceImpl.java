package com.lsxy.framework.oss.ali;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.oss.OSSService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tandy on 2016/7/14.
 * 阿里云OSS 云存储方案实现
 * repository ==> oss bucket
 *
 */
@Service
public class OSSServiceImpl implements OSSService{

    public static final Logger logger = LoggerFactory.getLogger(OSSServiceImpl.class);

    @Autowired
    private AliOSSClientFactoryBean afb;

    @Override
    public boolean uploadFileStream(InputStream inputStream, long fileLength, String orignalFileName, String repository, String fileKey) {
        if (logger.isDebugEnabled()){
            logger.debug("开始上传OSS文件{}({})",orignalFileName,fileKey);
        }
        long starttime = System.currentTimeMillis();
        try {
            OSSClient client = afb.getObject();
            client.putObject(repository, fileKey, inputStream);
        } catch (Exception e) {
            if (logger.isDebugEnabled()){
                logger.debug("上传OSS文件{}({})失败，花费时间{} ms，原因{}",orignalFileName,fileKey,(System.currentTimeMillis()-starttime),e);
            }
            logger.error("上传OSS文件失败",e);
            return false;
        }
        long endtime = System.currentTimeMillis();
        if (logger.isDebugEnabled()){
                logger.debug("上传OSS文件{}({})成功 花费时间 {} ms",orignalFileName,fileKey,(endtime-starttime));
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
            logger.error("上传文件异常",ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean uploadFileLocal(File file, String repository, String fileKey, String contentType, String contentDisposition) throws Exception {
        try {
            OSSClient client = afb.getObject();
            InputStream inputStream = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 设置上传文件长度
            meta.setContentLength(file.length());
            // 设置上传内容类型
            meta.setContentType(contentType);
            meta.setContentDisposition(contentDisposition);
            client.putObject(repository, fileKey, inputStream,meta);
        }catch(Exception ex){
            logger.error("上传文件异常",ex);
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

    @Override
    public List<String> deleteObjects(String repository,List<String> keys) throws Exception {
        OSSClient client = afb.getObject();
        List<String> deletedObjects = new ArrayList<>();
        if(keys.size()>1000) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < keys.size(); i++) {
                list.add(keys.get(i));
                if (list.size() % 1000 == 0) {//每1000个文件删除一次
                    list = new ArrayList();
                    DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(repository).withKeys(list));
                    List<String> re = deleteObjectsResult.getDeletedObjects();
                    deletedObjects.addAll(re);
                }
            }
            if (list.size() > 0) {//最后剩下的不足1000个文件也删除一次
                DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(repository).withKeys(list));
                List<String> re = deleteObjectsResult.getDeletedObjects();
                deletedObjects.addAll(re);
            }
        }else {
            //文件少于1000个文件时直接删除
            DeleteObjectsResult deleteObjectsResult = client.deleteObjects(new DeleteObjectsRequest(repository).withKeys(keys));
            List<String> re = deleteObjectsResult.getDeletedObjects();
            deletedObjects.addAll(re);
        }
        return deletedObjects;
    }


    public String uploadFile(String tenantId,String folder, MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();//文件名
        if(StringUtils.isNotBlank(name)){
            String type = name.substring(name.lastIndexOf("."),name.length());
            String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
            String fileKey = "tenant_res/"+tenantId+"/"+folder+"/"+ymd+"/"+ UUIDGenerator.uuid()+type;
            long size = file.getSize();
            boolean flag = this.uploadFileStream(file.getInputStream(),size,name, SystemConfig
                .getProperty("global.oss.aliyun.bucket"),fileKey);
            if(flag){
                return fileKey;
            }else{
                throw new RuntimeException("上传文件失败");
            }
        }else{
            return null;
        }
    }
}
