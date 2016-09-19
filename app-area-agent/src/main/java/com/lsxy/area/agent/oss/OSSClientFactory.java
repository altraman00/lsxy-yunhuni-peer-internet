package com.lsxy.area.agent.oss;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.oss.ali.AliOSSClientFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by zhangxb on 2016/9/17.
 */

public class OSSClientFactory extends AliOSSClientFactoryBean {

    @Override
    protected void initClient() {
        if (logger.isDebugEnabled()){
            logger.debug("OSSClientFactory创建ALI OSS链接客户端，区域创建的");
        }
        // endpoint以杭州为例，其它region请按实际情况填写
//        String endpoint = "http://oss-cn-beijing-internal.aliyuncs.com";
        String endpoint = SystemConfig.getProperty("global.oss.aliyun.endpoint.internet","http://oss-cn-beijing.aliyuncs.com");

        // accessKey请登录https://ak-console.aliyun.com/#/查看
        String accessKeyId = SystemConfig.getProperty("global.aliyun.key","nfgEUCKyOdVMVbqQ");
        String accessKeySecret = SystemConfig.getProperty("global.aliyun.secret","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW");
        // 创建OSSClient实例
        ossClient =buildOSSClient(accessKeyId,accessKeySecret,endpoint);
        if (logger.isDebugEnabled()){
            logger.debug("OSSClientFactory链接客户端成功：");
        }
    }
}
