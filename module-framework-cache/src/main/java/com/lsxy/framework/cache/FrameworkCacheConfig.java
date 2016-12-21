package com.lsxy.framework.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.framework.config.SystemConfig;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Map;

/**
 * Created by Tandy on 2016/6/25.
 */
@ComponentScan
@EnableCaching
@Configurable
public class FrameworkCacheConfig extends CachingConfigurerSupport {

    public static final Logger logger = LoggerFactory.getLogger(FrameworkCacheConfig.class);



    @Bean(name="lsxyRedisTemplate")
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {
        final RedisTemplate template = new RedisTemplate();
        template.setKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean(name="businessRedisTemplate")
//    @ConditionalOnMissingBean
    public RedisTemplate<String, String> hashAndSetRedisTemplate(
            RedisConnectionFactory factory) {
        final RedisTemplate template = new RedisTemplate();
        template.setKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());

        template.setHashValueSerializer(template.getStringSerializer());
        template.setValueSerializer(template.getStringSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public CacheManager cacheManager(@Qualifier("lsxyRedisTemplate") RedisTemplate lsxyRedisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(lsxyRedisTemplate);

        Map<String,String> expires = SystemConfig.getMapProperty("cache.redis.expires");
        Map<String,Long> expiresLong = new HashedMap();
        for (String key:expires.keySet()) {
            Long expireValue = Long.parseLong(expires.get(key));
            if (logger.isDebugEnabled()){
                    logger.debug("设置缓存过期时间->{}:{}",key,expireValue);
             }
            expiresLong.put(key,expireValue);
        }
        rcm.setExpires(expiresLong);

        return rcm;
    }


}
