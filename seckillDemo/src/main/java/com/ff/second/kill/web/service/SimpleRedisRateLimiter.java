package com.ff.second.kill.web.service;

import java.io.IOException;
import java.util.ResourceBundle;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author cornyu
 * @version 创建时间：2018年11月17日 下午9:50:49 类说明
 */

@Service
public class SimpleRedisRateLimiter implements IRedisRateLimiter,InitializingBean ,DisposableBean{
	protected static JedisPool jedispool;
	protected static int EXPIRE = 130;
	
	
	
	/**
	 * 利用redis实现限流功能
	 * period  时间间隔, 单位为秒
	 * maxCount  单位时间内限制的个数
	 */
	@Override
	public boolean isActionAllow(String userId, String actionKey, int period, int maxCount) throws IOException {
		Jedis jedis = getJedis();		
		String key = String.format("ff-yujy:%s:%s", userId, actionKey);
		long nowTs = System.currentTimeMillis();
		// 毫秒时间戳
		Pipeline pipeline = jedis.pipelined();
		pipeline.multi();// 用了multi，也就是事务，能保证一系列指令的原子顺序执行
		
		// value和score都使用毫秒时间戳
		pipeline.zadd(key, nowTs, nowTs + "");
		// 移除时间窗口之前的行为记录，剩下的都是时间窗口内的
		pipeline.zremrangeByScore(key, 0, nowTs - period * 1000);
		// 获得[nowTs-period*1000,nowTs]的key数量
		Response<Long> count = pipeline.zcard(key);
		// 每次设置都能保持更新key的过期时间
		pipeline.expire(key, period);
		
		pipeline.exec();
		pipeline.close();
		return count.get() <= maxCount;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initJedisPool();
	}
	
	public void initJedisPool() {
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
	
	public Jedis getJedis() {
		if(jedispool != null) {
			initJedisPool();
		}
		
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
	
	

	@Override
	public void destroy() throws Exception {
		if(jedispool != null) {
			jedispool.close();
		}
	}
	
	
}
