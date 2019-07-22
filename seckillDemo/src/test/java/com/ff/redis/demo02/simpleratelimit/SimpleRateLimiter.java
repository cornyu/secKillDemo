package com.ff.redis.demo02.simpleratelimit;

import java.io.IOException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * @author cornyu
 * @version 创建时间：2018年11月17日 下午4:41:40 
 * 使用redis实现简单限流功能
 */
public class SimpleRateLimiter {
	private final Jedis jedis;

	public SimpleRateLimiter(Jedis jedis) {
		this.jedis = jedis;
	}

	public boolean isActionAllow(String userId, String actionKey, int period, int maxCount) throws IOException {
		String key = String.format("hist6:%s:%s", userId, actionKey);
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

	public static void main(String[] args) throws IOException, InterruptedException {
		Jedis jedis = new Jedis("127.0.0.1");
		SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
		for (int i = 0; i < 200; i++) {
			// 每个用户在1秒内最多能做五次动作
			System.out.println(limiter.isActionAllow("userId", "actionkey", 1, 10));
		}
	}
}
