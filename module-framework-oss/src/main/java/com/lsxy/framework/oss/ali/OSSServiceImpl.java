package com.lsxy.framework.oss.ali;

import com.lsxy.framework.oss.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Tandy on 2016/7/14.
 * 阿里云OSS 云存储方案实现
 * repository ==> oss bucket
 *
 */
@Service
@Lazy(true)
public class OSSServiceImpl implements OSSService{

    @Autowired
    private AliOSSClientFactoryBean afb;


    @Override
    public String uploadFileStream(InputStream stream, long fileLength, String orignalFileName, String repository, String destFileName, String path) {

        return null;
    }

    @Override
    public String uploadFileLocal(File file, String repository, String path, String destFileName) {
        return null;
    }

    @Override
    public InputStream getFileStream(String repository, String path, String fileName) {
        return null;
    }

    @Override
    public String getFilePublicUrl(String repository, String path, String fileName) {
        return null;
    }


}
