package com.ff.redis.util;

import java.util.ResourceBundle;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author cornyu
 * @version 创建时间：2018年11月11日 下午9:55:25 
 * 
 * 类说明
 */
public class RedisProvider {
	// private static Logger logger = LoggerFactory.getLogger(RedisProvider.class);

	protected static JedisPool jedispool;
	protected static int EXPIRE = 130;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}

		EXPIRE = Integer.valueOf(bundle.getString("redis.expire"));

		JedisPoolConfig jedisconfig = new JedisPoolConfig();
		jedisconfig.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
		jedisconfig.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
		jedisconfig.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
		jedisconfig.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
		jedisconfig.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
		jedispool = new JedisPool(jedisconfig, bundle.getString("redis.ip"),
				Integer.valueOf(bundle.getString("redis.port")), 100000);

	}

	public static Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedispool.getResource();
		} catch (JedisConnectionException jce) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {

			}
			jedis = jedispool.getResource();
		}
		return jedis;
	}

	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedispool.returnResource(jedis);
		}
	}

	
	
	public static void main(String args[]) {

	}

}
