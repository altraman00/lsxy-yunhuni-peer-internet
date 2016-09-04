package com.lsxy.framework.cache;

import com.lsxy.framework.config.SystemConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Tandy on 2016/6/25.
 */
@ComponentScan
public class FrameworkCacheConfigNotSpringBoot {

    private JedisConnectionFactory jedisConnectionFactory;

    @Bean(name="jedisConnectionFactory")
    @ConditionalOnMissingBean
    public JedisConnectionFactory getJedisConnectionFactory(){
        jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPassword(SystemConfig.getProperty("spring.redis.password",""));
        jedisConnectionFactory.setHostName(SystemConfig.getProperty("spring.redis.host","localhost"));
        jedisConnectionFactory.setPort(Integer.parseInt(SystemConfig.getProperty("spring.redis.port","6379")));
        jedisConnectionFactory.setPoolConfig(getJedisPoolConfig());
        return jedisConnectionFactory;
    }


    @Bean
    @DependsOn("jedisConnectionFactory")
    public RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }


    public JedisPoolConfig getJedisPoolConfig(){
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxWaitMillis(Long.parseLong(SystemConfig.getProperty("spring.redis.pool.max-wait","-1")));
        jpc.setMaxIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.max-idle","8")));
        jpc.setMinIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.min-idle","0")));
        jpc.setMaxTotal(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.max-active","8")));
        return jpc;
    }



}
