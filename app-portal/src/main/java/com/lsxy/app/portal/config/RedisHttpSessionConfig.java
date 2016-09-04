package com.lsxy.app.portal.config;

import com.lsxy.framework.config.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

import static com.sun.jmx.snmp.EnumRowStatus.active;

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
