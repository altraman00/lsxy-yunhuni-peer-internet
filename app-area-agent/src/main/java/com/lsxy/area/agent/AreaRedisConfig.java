package com.lsxy.area.agent;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tandy on 16/8/26.
 */
@Configuration
@ConfigurationProperties(prefix = "area.agent.redis")
public class AreaRedisConfig extends RedisProperties{

    private RedisSentinelConfiguration sentinelConfiguration = null;

    @Bean
    public JedisConnectionFactory redisConnectionFactory()
            throws UnknownHostException {
        return applyProperties(createJedisConnectionFactory());
    }

    protected final JedisConnectionFactory applyProperties(
            JedisConnectionFactory factory) {
        factory.setHostName(this.getHost());
        factory.setPort(this.getPort());
        if (this.getPassword() != null) {
            factory.setPassword(this.getPassword());
        }
        factory.setDatabase(this.getDatabase());
        if (this.getTimeout() > 0) {
            factory.setTimeout(this.getTimeout());
        }
        return factory;
    }
    private JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        RedisProperties.Pool props = this.getPool();
        config.setMaxTotal(props.getMaxActive());
        config.setMaxIdle(props.getMaxIdle());
        config.setMinIdle(props.getMinIdle());
        config.setMaxWaitMillis(props.getMaxWait());
        return config;
    }

    private JedisConnectionFactory createJedisConnectionFactory() {
        if (this.getPool() != null) {
            return new JedisConnectionFactory(getSentinelConfig(), jedisPoolConfig());
        }
        return new JedisConnectionFactory(getSentinelConfig());
    }

    protected final RedisSentinelConfiguration getSentinelConfig() {
        if (this.sentinelConfiguration != null) {
            return this.sentinelConfiguration;
        }
        Sentinel sentinelProperties = this.getSentinel();
        if (sentinelProperties != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(sentinelProperties.getMaster());
            config.setSentinels(createSentinels(sentinelProperties));
            return config;
        }
        return null;
    }


    private List<RedisNode> createSentinels(Sentinel sentinel) {
        List<RedisNode> sentinels = new ArrayList<RedisNode>();
        String nodes = sentinel.getNodes();
        for (String node : StringUtils.commaDelimitedListToStringArray(nodes)) {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                sentinels.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            }
            catch (RuntimeException ex) {
                throw new IllegalStateException(
                        "Invalid redis sentinel " + "property '" + node + "'", ex);
            }
        }
        return sentinels;
    }
}
