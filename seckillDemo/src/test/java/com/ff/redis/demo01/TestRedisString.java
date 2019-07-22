package com.ff.redis.demo01;

import redis.clients.jedis.Jedis;

/**
 * @author cornyu
 * @version 创建时间：2018年11月11日 下午9:26:16 类说明
 *          Redis的字符串是动态字符串，是可以修改的字符串，内部结构实现上类似于Java的ArrayList，
 *          采用预分配冗余空间的方式来减少内存的频繁分配。
 *          当字符串长度小于1M时，扩容都是加倍现有的空间，如果超过1M，扩容时一次只会多扩1M的空间。需要注意的是字符串最大长度为512M。
 * 
 *          链接：https://juejin.im/post/5b53ee7e5188251aaa2d2e16
 * 
 *          redis基本数据类型: string hash list set zset
 * 
 */
public class TestRedisString {

	public static void main(String[] args) {
		// 本地先起redis服务,默认端口号为6379

		Jedis jedis = new Jedis("127.0.0.1", 6379);

		// string
		jedis.set("name", "cornyuyjy has a great idea");
		System.out.println("name:" + jedis.get("name"));

		// strlen 获取长度
		jedis.strlen("name");

		// 获取子串
		jedis.getrange("name", 2, 3);

		// 覆盖
		jedis.setrange("name", 5, "78");

		// 追加
		jedis.append("name", " append content");
		System.out.println("name:" + jedis.get("name"));

		// 计数器功能,计数器是有范围的，它不能超过Long.Max，不能低于Long.MIN

		jedis.set("count", "3");
		System.out.println("count:" + jedis.get("count"));

		jedis.incrBy("count", 10);
		System.out.println("count:" + jedis.get("count"));

		//jedis.del("name");
		//jedis.del("count");
		
		jedis.close();

	}

}
