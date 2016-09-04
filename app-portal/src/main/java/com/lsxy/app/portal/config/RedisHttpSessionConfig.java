package com.lsxy.app.portal.config;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by tandy on 16/9/2.
 */
@EnableRedisHttpSession
public class RedisHttpSessionConfig {
//    @Bean
//    public JedisConnectionFactory connectionFactory() {
//        JedisPoolConfig jpc = new JedisPoolConfig();
//        jpc.setMaxIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.max-idle","8")));
//        jpc.setMinIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.min-idle","0")));
//        jpc.setMaxWaitMillis(Long.parseLong(SystemConfig.getProperty("spring.redis.pool.max-wait","-1")));
//        JedisConnectionFactory f = new JedisConnectionFactory(jpc);
//        f.setHostName(SystemConfig.getProperty("spring.redis.host","localhost"));
//        f.setPort(Integer.parseInt(SystemConfig.getProperty("spring.redis.port","6379")));
//        return f;
//    }

}
