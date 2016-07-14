package com.lsxy.framework.oss.ali;

import com.aliyun.oss.OSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/14.
 * 阿里云OSSClient工厂类
 */
@Component
public class AliOSSClientFactoryBean implements FactoryBean<OSSClient>,InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(AliOSSClientFactoryBean.class);
    
    private OSSClient ossClient;

    @Override
    public OSSClient getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return OSSClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (logger.isDebugEnabled()){
                logger.debug("创建ALI OSS链接客户端");
         }
    // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // accessKey请登录https://ak-console.aliyun.com/#/查看
        String accessKeyId = "<yourAccessKeyId>";
        String accessKeySecret = "<yourAccessKeySecret>";
        // 创建OSSClient实例
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        if (logger.isDebugEnabled()){
                logger.debug("链接客户端成功：");
         }

    }
}
