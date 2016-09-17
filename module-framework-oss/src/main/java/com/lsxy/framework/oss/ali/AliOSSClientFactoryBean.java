package com.lsxy.framework.oss.ali;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.lsxy.framework.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/14.
 * 阿里云OSSClient工厂类
 */
@Component
@ConditionalOnMissingBean(AliOSSClientFactoryBean.class)
public class AliOSSClientFactoryBean implements FactoryBean<OSSClient>,InitializingBean,DisposableBean {

    public static final Logger logger = LoggerFactory.getLogger(AliOSSClientFactoryBean.class);
    
    protected OSSClient ossClient;

    @Override
    public OSSClient getObject() throws Exception {
        return this.ossClient;
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
        initClient();
    }

    protected void initClient(){
        if (logger.isDebugEnabled()){
            logger.debug("创建ALI OSS链接客户端");
        }
        // endpoint以杭州为例，其它region请按实际情况填写
//        String endpoint = "http://oss-cn-beijing-internal.aliyuncs.com";
        String endpoint = SystemConfig.getProperty("global.oss.aliyun.endpoint","http://oss-cn-beijing.aliyuncs.com");

        // accessKey请登录https://ak-console.aliyun.com/#/查看
        String accessKeyId = SystemConfig.getProperty("global.aliyun.key","nfgEUCKyOdVMVbqQ");
        String accessKeySecret = SystemConfig.getProperty("global.aliyun.secret","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW");
        // 创建OSSClient实例
        ossClient =buildOSSClient(accessKeyId,accessKeySecret,endpoint);
        if (logger.isDebugEnabled()){
            logger.debug("链接客户端成功：");
        }
    }
    @Override
    public void destroy() throws Exception {
        if(ossClient != null){
            ossClient.shutdown();
        }
    }



    /**
     * 构建一个OSSCLIENT
     * @param endpoint
     * @param secret
     * @param key
     * @return
     */
    protected static OSSClient buildOSSClient(String key, String secret, String endpoint){
        // 创建ClientConfiguration实例
        ClientConfiguration conf = new ClientConfiguration();
        // 设置HTTP最大连接数为10
        conf.setMaxConnections(10);

        // 设置TCP连接超时为5000毫秒
        conf.setConnectionTimeout(5000);

        // 设置最大的重试次数为3
        conf.setMaxErrorRetry(3);

        // 设置Socket传输数据超时的时间为2000毫秒
        conf.setSocketTimeout(2000);

//    	if(SystemConfig.getProperty("http.proxy.enable","false").equals("true")){
//    		log.debug("use proxy build ossclient");
//    		// 配置代理为本地8080端口
//        	conf.setProxyHost(SystemConfig.getProperty("http.proxy.hostname"));
//        	conf.setProxyPort(Integer.parseInt(SystemConfig.getProperty("http.proxy.port")));
//        	//设置用户名和密码
//        	conf.setProxyUsername(SystemConfig.getProperty("http.proxy.username"));
//        	conf.setProxyPassword(SystemConfig.getProperty("http.proxy.passwd"));
//    	}
        // 初始化OSSClient
        OSSClient client = new OSSClient(endpoint, key, secret,conf);
        return client;
    }
}
