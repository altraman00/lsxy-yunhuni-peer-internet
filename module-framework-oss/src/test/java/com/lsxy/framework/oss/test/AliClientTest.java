package com.lsxy.framework.oss.test;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.oss.FrameworkOSSConfig;
import com.lsxy.framework.oss.OSSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Tandy on 2016/7/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FrameworkOSSConfig.class)
public class AliClientTest {
    @Autowired
    private OSSService ossService;

    @Test
    public void test001() throws Exception {
        Assert.notNull(ossService);
        String fileKey = "images/20141018_163328.jpg";
        String repository="yunhuni-development";
        List<String> objs =  ossService.listObjects(repository,"images/");
        Assert.notNull(objs);
        Assert.isTrue(objs.size()>0);
//        InputStream is = ossService.getFileStream(repository,fileKey);
//        File file = new File("./a.jpg");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//        FileOutputStream fos = new FileOutputStream(file);
//        FileUtil.copyStream(is,fos);
//        fos.flush();
//        fos.close();
//        System.out.println(file.getAbsolutePath());
//        Assert.notNull(is);
////        FileUtil.delFile(file.getAbsolutePath());
    }

    @Test
    public void testDownload() throws Exception {
        String fileKey = "images/20141018_163328.jpg";
        String repository="yunhuni-development";
        String destFile = "d:/a.jpg";
        File file = this.ossService.downLoadFile(repository,fileKey,destFile);
        Assert.notNull(file);
        Assert.isTrue(file.length()>0);
    }

    @Test
    public void testUploadLocalFile() throws Exception {
        String fileKey = "images/cc.png";
        String repository="yunhuni-development";
        String destFile = "d:/cc.png";

        boolean result = this.ossService.uploadFileLocal(new File(destFile),repository,fileKey);
        Assert.isTrue(result);
    }

    @Test
    public void testUploadStreamFile() throws FileNotFoundException {
        String fileKey = "images/cc22004.png";
        String repository="yunhuni-development";
        String destFile = "d:/cc.png";
        InputStream is = new FileInputStream(new File(destFile));
        boolean result = this.ossService.uploadFileStream(is,100,destFile,repository,fileKey);
        Assert.isTrue(result);
    }

    @Test
    public void testDeleteFile(){
        String fileKey = "images/cc22004.png";
        String repository="yunhuni-development";
        this.ossService.deleteObject(repository,fileKey);
    }
    @Test
    public void testUploadFileLocal() throws Exception {
        String ossUri = "images/cc22004.png";
        String repository="yunhuni-development";
        String destFile = "d:/cc.png";
        String fileName = UUIDGenerator.uuid() + ".zip";
        ossService.uploadFileLocal(new File(destFile), repository, ossUri, "application/octet-stream", fileName);
    }
}
