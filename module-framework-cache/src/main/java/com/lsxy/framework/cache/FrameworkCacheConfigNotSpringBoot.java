package com.lsxy.framework.cache;

/**
 * Created by Tandy on 2016/6/25.
 */
//@ComponentScan
//public class FrameworkCacheConfigNotSpringBoot {
//
//    private JedisConnectionFactory jedisConnectionFactory;
//
//    @Bean(name="jedisConnectionFactory")
//    @ConditionalOnMissingBean
//    public JedisConnectionFactory getJedisConnectionFactory(){
//        jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setPassword(SystemConfig.getProperty("spring.redis.password",""));
//        jedisConnectionFactory.setHostName(SystemConfig.getProperty("spring.redis.host","localhost"));
//        jedisConnectionFactory.setPort(Integer.parseInt(SystemConfig.getProperty("spring.redis.port","6379")));
//        jedisConnectionFactory.setPoolConfig(getJedisPoolConfig());
//        return jedisConnectionFactory;
//    }
//
//    @Bean(name="lsxyRedisTemplate")
//    public RedisTemplate redisTemplate(
//            RedisConnectionFactory factory) {
//        final RedisTemplate template = new RedisTemplate();
//        template.setKeySerializer(template.getStringSerializer());
//        template.setHashKeySerializer(template.getStringSerializer());
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setConnectionFactory(factory);
//        return template;
//    }
//
//
//    @Bean(name="businessRedisTemplate")
//    public RedisTemplate<String, String> hashAndSetRedisTemplate(
//            RedisConnectionFactory factory) {
//        final RedisTemplate template = new RedisTemplate();
//        template.setKeySerializer(template.getStringSerializer());
//        template.setHashKeySerializer(template.getStringSerializer());
//
//        template.setHashValueSerializer(template.getStringSerializer());
//        template.setValueSerializer(template.getStringSerializer());
//        template.setConnectionFactory(factory);
//        return template;
//    }
//
//    public JedisPoolConfig getJedisPoolConfig(){
//        JedisPoolConfig jpc = new JedisPoolConfig();
//        jpc.setMaxWaitMillis(Long.parseLong(SystemConfig.getProperty("spring.redis.pool.max-wait","-1")));
//        jpc.setMaxIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.max-idle","8")));
//        jpc.setMinIdle(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.min-idle","0")));
//        jpc.setMaxTotal(Integer.parseInt(SystemConfig.getProperty("spring.redis.pool.max-active","8")));
//        return jpc;
//    }
//
//
//
//}
