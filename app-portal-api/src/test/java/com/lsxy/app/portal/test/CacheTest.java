package com.lsxy.app.portal.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Created by Tandy on 2016/7/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
@WebIntegrationTest("server.port:0")
public class CacheTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    private RedisCacheService redisCacheService;

    @Value("http://localhost:${local.server.port}/")
    private String restPrefixUrl;

    public static final Logger logger = LoggerFactory.getLogger(CacheTest.class);


    private Account getAccountInCache(String id) {
        Account result = null;
        String json = redisCacheService.get("entity_"+id);
        if(StringUtil.isNotEmpty(json)){
            try {
                Jackson2JsonRedisSerializer j2j = new Jackson2JsonRedisSerializer(Object.class);
                ObjectMapper om = new ObjectMapper();
                om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                j2j.setObjectMapper(om);
                result = (Account) j2j.deserialize(json.getBytes());
            }catch(Exception ex){
                logger.error("反序列化失败：",ex);
            }
        }
        return result;
    }


    @Test
    public void testSaveCache(){
        //创建缓存
        String id = "1";
        String url = restPrefixUrl + "open/test/cache/account/"+id;
        RestResponse<Account> response = RestRequest.buildRequest().get(url,Account.class);
        Assert.notNull(response.getData());
        Account account = getAccountInCache(id);
        Assert.notNull(account);
        Assert.isTrue(account.getId().equals(id));
        String userOriName = account.getUserName();

        //更新缓存
        String userName="修改了啊";
        String saveUrl = restPrefixUrl + "open/test/cache/account/save/"+id+"?userName={1}";
        RestResponse<String> responsex = RestRequest.buildRequest().get(saveUrl,String.class,userName);
        Assert.isTrue(responsex.getData().equals("OK"));
        account = getAccountInCache(id);
        Assert.isTrue(account.getUserName().equals(userName));
        responsex = RestRequest.buildRequest().get(saveUrl,String.class,userOriName);
        Assert.isTrue(responsex.getData().equals("OK"));


        //删除缓存
        String delUrl = restPrefixUrl + "open/test/cache/account/del/"+id;
        RestResponse<String> response4 = RestRequest.buildRequest().get(delUrl,String.class);
        Assert.isTrue(response4.getData().equals("OK"));
        account = getAccountInCache(id);
        Assert.isNull(account);


    }



}
