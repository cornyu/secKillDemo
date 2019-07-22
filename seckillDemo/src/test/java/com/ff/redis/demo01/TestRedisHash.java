package com.ff.redis.demo01;

import java.util.HashMap;
import java.util.Map;


import redis.clients.jedis.Jedis;

/**
 * @author cornyu
 * @version 创建时间：2019年6月7日 上午9:54:39 
 * 类说明
 * 哈希等价于Java语言的HashMap或者是Python语言的dict，在实现结构上它使用二维结构，第一维是数组，第二维是链表，
 * hash的内容key和value存放在链表中，数组里存放的是链表的头指针。通过key查找元素时，先计算key的hashcode，
 * 然后用hashcode对数组的长度进行取模定位到链表的表头，再对链表进行遍历获取到相应的value值，
 * 链表的作用就是用来将产生了「hash碰撞」的元素串起来。Java语言开发者会感到非常熟悉，因为这样的结构和HashMap是没有区别的。
 * 哈希的第一维数组的长度也是2^n。
 * 
 * 扩容 
 * 当hash内部的元素比较拥挤时(hash碰撞比较频繁)，就需要进行扩容。扩容需要申请新的两倍大小的数组，然后将所有的键值对重新分配到新的数组下标对应的链表中(rehash)。
 * 如果hash结构很大，比如有上百万个键值对，那么一次完整rehash的过程就会耗时很长。这对于单线程的Redis里来说有点压力山大。
 * 所以Redis采用了渐进式rehash的方案。它会同时保留两个新旧hash结构，在后续的定时任务以及hash结构的读写指令中将旧结构的元素逐渐迁移到新的结构中。
 * 这样就可以避免因扩容导致的线程卡顿现象。

   缩容
 Redis的hash结构不但有扩容还有缩容，从这一点出发，它要比Java的HashMap要厉害一些，Java的HashMap只有扩容。
 缩容的原理和扩容是一致的，只不过新的数组大小要比旧数组小一倍。

链接：https://juejin.im/post/5b53ee7e5188251aaa2d2e16
 * 
 */
public class TestRedisHash {

	public static void main(String[] args) {
		// 本地先起redis服务,默认端口号为6379

		Jedis jedis = new Jedis("127.0.0.1", 6379);
		jedis.hset("hashKey", "name", "cornyu");
		
		Map<String, String> personMap = new HashMap<String, String>();
		personMap.put("age", "18");
		personMap.put("sex", "man");
		
		jedis.hmset("hashKey", personMap);
		System.out.println(jedis.hlen("hashKey"));//3
		
		System.out.println(jedis.hgetAll("hashKey"));
		
		System.out.println(jedis.hkeys("hashKey"));
		
		System.out.println(jedis.hvals("hashKey"));
		
		//删除 元素
		jedis.hdel("hashKey", "name");
		System.out.println(jedis.hgetAll("hashKey"));
		
		//判断元素是否存在
		System.out.println(jedis.hexists("hashKey", "name"));//false

		
		//计数器 hash结构还可以当成计数器来使用，对于内部的每一个key都可以作为独立的计数器。
		//如果value值不是整数，调用hincrby指令会出错。
		
		jedis.hincrBy("hashKey", "age", 10);
		System.out.println(jedis.hgetAll("hashKey"));//{sex=man, age=28}


		
		
		jedis.close();
		
		
	}

}
