package com.ff.redis.demo01;

import redis.clients.jedis.Jedis;

/** 
* @author cornyu 
* @version 创建时间：2019年6月7日 上午10:52:12 
* 类说明 
* 
* Java程序员都知道HashSet的内部实现使用的是HashMap，只不过所有的value都指向同一个对象。
* Redis的set结构也是一样，它的内部也使用hash结构，所有的value都指向同一个内部值。
*/
public class TestRedisSet {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		
		jedis.sadd("lgSet", "java");
		jedis.sadd("lgSet", "py");
		jedis.sadd("lgSet", "c++");
		jedis.sadd("lgSet", "c#");
		jedis.sadd("lgSet", "c#");

		//读取元素 使用smembers列出所有元素，使用scard获取集合长度，使用srandmember获取随机count个元素，
		//如果不提供count参数，默认为1
		System.out.println(jedis.smembers("lgSet"));
		System.out.println(jedis.scard("lgSet"));
		System.out.println(jedis.srandmember("lgSet"));
		
		//删除元素 使用srem删除一到多个元素，使用spop删除随机一个元素
		System.out.println(jedis.srem("lgSet", "c#"));

		//判断元素是否存在 使用sismember指令，只能接收单个元素
		System.out.println(jedis.sismember("lgSet", "c#"));

		
		
		jedis.close();

	}

}
