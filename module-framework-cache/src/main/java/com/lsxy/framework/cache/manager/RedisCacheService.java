package com.lsxy.framework.cache.manager;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Redis操作方法
 * @author tandy
 *
 */
@Component
@DependsOn("lsxyRedisTemplate")
@SuppressWarnings({"unchecked","rawtypes"})
public class RedisCacheService {
	
	  //加锁标志
//    public static final String LOCKED = "TRUE";
//    public static final long ONE_MILLI_NANOS = 1000000L;
    //默认超时时间（毫秒）
//    public static final long DEFAULT_TIME_OUT = 3000;

	private final static Log logger = LogFactory.getLog(RedisCacheService.class);

	@Autowired
	@Qualifier("lsxyRedisTemplate")
    private RedisTemplate redisTemplate;
    
	 private static String redisCode = "utf-8";

	@PostConstruct
	public void init(){
	}

	/**
	 * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
	 * @param key
	 * @param value
	 * @throws TransactionExecFailedException
	 */
	public void setTransactionFlag(final String key, final String value,final long expire)
			throws TransactionExecFailedException {
		boolean result = (boolean) redisTemplate
				.execute(new RedisCallback() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						logger.debug("ready to set nx:"+key+">>>>"+ value);
						boolean ret = connection.setNX(key.getBytes(), value.getBytes());
						//默认缓存2天
						connection.expire(key.getBytes(), expire);
						logger.debug("set nx result:"+ret);
						return ret;
					}

				});
		//如果结果为空表示设置失败了
		if(result == false)
			throw new TransactionExecFailedException();
	}

	    /**
	     * @param keys
	     */
	    
		public long del(final String... keys) {
	        return (long) redisTemplate.execute(new RedisCallback() {
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	                long result = 0;
	                for (int i = 0; i < keys.length; i++) {
	                    result = connection.del(keys[i].getBytes());
	                }
	                return result;
	            }
	        });
	    }

	    /**
	     * @param key
	     * @param value
	     * @param liveTime
	     */
	    public void set(final byte[] key, final byte[] value, final long liveTime) {
	    	redisTemplate.execute(new RedisCallback() {
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	            	
	                connection.set(key, value);
	                if (liveTime > 0) {
	                    connection.expire(key, liveTime);
	                }
	                return 1L;
	            }
	        });
	    }

	/**
	 * @param key
	 * @param liveTime
	 */
	public void expire(final byte[] key,final long liveTime) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	    /**
	     * @param key
	     * @param value
	     * @param liveTime  过期时间单位为秒
	     */
	    public void set(String key, String value, long liveTime) {
	        this.set(key.getBytes(), value.getBytes(), liveTime);
	    }

        /**
         * @param key
         * @param liveTime  过期时间单位为秒
         */
        public void expire(String key, long liveTime) {
            this.expire(key.getBytes(), liveTime);
        }

	    /**
	     * @param key
	     * @param value
	     */
	    public void set(String key, String value) {

	        this.set(key, value, 0L);
	    }

	    /**
	     * @param key
	     * @param value
	     */
	    public void set(byte[] key, byte[] value) {
	        this.set(key, value, 0L);
	    }

	    /**
	     * @param key
	     * @return
	     */
	    public String get(final String key) {
	        String result = (String) redisTemplate.execute(new RedisCallback() {
	            public String doInRedis(RedisConnection connection) throws DataAccessException {
	                try {
	                	byte[] obj = connection.get(key.getBytes());
	                	if(obj != null){
	                		return new String(obj, redisCode);
	                	}else{
	                		return null;
	                	}
	                } catch (UnsupportedEncodingException e) {
						logger.error("不支持的编码转换",e);
	                }
	                return null;
	            }
	        });
	    	logger.debug("get cache value:"+key+"=>"+result);
	        return result;
	    }

	    /**
	     * @param pattern
	     * @return
	     */
	    public Set keys(String pattern) {
	        return redisTemplate.keys(pattern);

	    }

	    /**
	     * @param key
	     * @return
	     */
	    public boolean exists(final String key) {
	        return (boolean) redisTemplate.execute(new RedisCallback() {
	            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
	                return connection.exists(key.getBytes());
	            }
	        });
	    }

	    /**
	     * @return
	     */
	    public String flushDB() {
	        return (String) redisTemplate.execute(new RedisCallback() {
	            public String doInRedis(RedisConnection connection) throws DataAccessException {
	                connection.flushDb();
	                return "ok";
	            }
	        });
	    }

	    /**
	     * @return
	     */
	    public long dbSize() {
	        return (long) redisTemplate.execute(new RedisCallback() {
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	                return connection.dbSize();
	            }
	        });
	    }

	    /**
	     * @return
	     */
	    public String ping() {
	        return (String) redisTemplate.execute(new RedisCallback() {
	            public String doInRedis(RedisConnection connection) throws DataAccessException {

	                return connection.ping();
	            }
	        });
	    }

	    /**
	     * 执行设置值，如果由于并发导致设置标记位导致设置失败，丢出TransactionExecFailedException异常
	     * @param key
	     * @param value 
	     * @throws TransactionExecFailedException
	     */
	    public void setTransactionFlag(final String key, final String value)
				throws TransactionExecFailedException {
				boolean result = (boolean) redisTemplate
						.execute(new RedisCallback() {
							@Override
							public Object doInRedis(RedisConnection connection)
									throws DataAccessException {
								logger.debug("ready to set nx:"+key+">>>>"+ value);
								boolean ret = connection.setNX(key.getBytes(), value.getBytes());
								//默认缓存2天
								connection.expire(key.getBytes(), 48*60*60);
								logger.debug("set nx result:"+ret);
								return ret;
							}

						});
				//如果结果为空表示设置失败了
				if(result == false)
					throw new TransactionExecFailedException();
		}

	    
	    /**
	     * 针对redis incr命令的封装，实现指定key的值自增长
	     * @param key
	     * 	key值
	     * @return
	     *  自增长后的值
	     */
		public long incr(final String key) {
			long result = (long) redisTemplate
					.execute(new RedisCallback() {
						@Override
						public Object doInRedis(RedisConnection connection)
								throws DataAccessException {
							return connection.incr(key.getBytes());
						}

					});
			return result;
		}

		/**
		 * 针对redis INCRBY，实现指定key的值的增长
		 * @param key 	key值
		 * @param incr 增长的值
		 * @return
		 *  自增长后的值
		 */
		public long incrBy(final String key,Long incr) {
			long result = (long) redisTemplate
					.execute(new RedisCallback() {
						@Override
						public Object doInRedis(RedisConnection connection)
								throws DataAccessException {
							return connection.incrBy(key.getBytes(),incr);
						}

					});
			return result;
		}

		/**
		 * 获取缓存的值，之后迅速删除掉
		 * @param key
		 * 	缓存key
		 * @return
		 * 	返回指定key对应的值
		 */
		public String getAndRemove(final String key) {
			 return (String) redisTemplate.execute(new RedisCallback() {
		            public String doInRedis(RedisConnection connection) throws DataAccessException {
		                try {
		                	byte[] obj = connection.get(key.getBytes());
		                	if(obj != null){
		                		connection.del(key.getBytes());
		                		return new String(obj, redisCode);
		                	}else{
		                		return null;
		                	}
		                } catch (UnsupportedEncodingException e) {
							logger.error("不支持的编码转换",e);
		                }
		                return null;
		            }
		        });
		}

		/**
		 * 
		 * @param key
		 * @param queueid
		 */
		public void zrem(final String key, final String queueid) {
			redisTemplate.opsForZSet().remove(key, queueid);
		}

		public void zrem(String key, Object... array) {
			redisTemplate.opsForZSet().remove(key, array);
			
		}

		public Set zrang(final String key, final long start,final long end) {
			return redisTemplate.opsForZSet().range(key, start, end);
		}

		public Set zrang_score(final String key, final long min,final long max) {
			return redisTemplate.opsForZSet().rangeByScore(key, min, max);
		}

		public void zadd(String key,String value,double score) {
			redisTemplate.opsForZSet().add(key, value, score);
			
		}

		public void sadd(final String key, final Object ... values) {
			redisTemplate.opsForSet().add(key, values);
		}
		
		public Set smembers(final String key) {
			Set set = redisTemplate.opsForSet().members(key);
			return set;
		}

	public BoundHashOperations getHashOps(String key){
        return redisTemplate.boundHashOps(key);
    }
}
