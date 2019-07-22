package com.ff.redis.demo01;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

/** 
* @author cornyu 
* @version 创建时间：2019年6月7日 上午10:58:56 
* 类说明 
* 
* SortedSet(zset)是Redis提供的一个非常特别的数据结构，一方面它等价于Java的数据结构Map<String, Double>，
* 可以给每一个元素value赋予一个权重score，另一方面它又类似于TreeSet，内部的元素会按照权重score进行排序，
* 可以得到每个元素的名次，还可以通过score的范围来获取元素的列表。
zset底层实现使用了两个数据结构，第一个是hash，第二个是跳跃列表，hash的作用就是关联元素value和权重score，
保障元素value的唯一性，可以通过元素value找到相应的score值。跳跃列表的目的在于给元素value排序，根据score的范围获取元素列表。

链接：https://juejin.im/post/5b53ee7e5188251aaa2d2e16
*/
public class TestRedisZSet {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);

		
		//增加元素 通过zadd指令可以增加一到多个value/score对，score放在前面
		Map<String, Double> scoreMembers = new HashMap<>();
		scoreMembers.put("java", Double.valueOf(99) );
		scoreMembers.put("python", Double.valueOf(98) );
		scoreMembers.put("c++", Double.valueOf(90) );
		scoreMembers.put("c#", Double.valueOf(80) );
		scoreMembers.put("js", Double.valueOf(88) );

		jedis.zadd("zset", scoreMembers);
		
		//长度 通过指令zcard可以得到zset的元素个数
		System.out.println(jedis.zcard("zset"));
		
		//删除元素 通过指令zrem可以删除zset中的元素，可以一次删除多个
		jedis.zrem("zset", "js");
		System.out.println(jedis.zcard("zset"));
		
		jedis.zincrby("zset", 1, "c#");
		
		//获取排名和分数 
		//通过zscore指令获取指定元素的权重，
		//通过zrank指令获取指定元素的正向排名，
		//通过zrevrank指令获取指定元素的反向排名[倒数第一名]。正向是由小到大，负向是由大到小。
		System.out.println(jedis.zscore("zset", "c#"));
		System.out.println(jedis.zrank("zset", "java"));//3
		System.out.println(jedis.zrank("zset", "c#"));//0

		System.out.println(jedis.zrevrank("zset", "java"));//0
		System.out.println(jedis.zrevrank("zset", "c#"));//3
		
		//根据排名范围获取元素列表 通过zrange指令指定排名范围参数获取对应的元素列表，携带withscores参数可以一并获取元素的权重。
		//通过zrevrange指令按负向排名获取元素列表[倒数]。正向是由小到大，负向是由大到小。
		System.out.println(jedis.zrange("zset", 0, -1)); //[c#, c++, python, java]  从小到大
		
		System.out.println(jedis.zrangeWithScores("zset", 0, -1));//[[[99, 35],81.0], [[99, 43, 43],90.0], [[112, 121, 116, 104, 111, 110],98.0], [[106, 97, 118, 97],99.0]]

		//根据score范围获取列表 通过zrangebyscore指令指定score范围获取对应的元素列表。通过zrevrangebyscore指令获取倒排元素列表。
		//正向是由小到大，负向是由大到小。参数-inf表示负无穷，+inf表示正无穷。
		System.out.println(jedis.zrangeByScore("zset", 98, 99));//[python, java]

		
		System.out.println(jedis.zcount("zset", 98, 99));//2
		
		//删除 
		//System.out.println(jedis.zremrangeByRank("zset", 0, -1));


	}

}
