package com.ff.redis.demo01;


import redis.clients.jedis.Jedis;

/**
 * @author cornyu
 * @version 创建时间：2019年6月7日 上午9:55:44 
 * 类说明
 * Redis将列表数据结构命名为list而不是array，是因为列表的存储结构用的是链表而不是数组，而且链表还是双向链表。
 * 因为它是链表，所以随机定位性能较弱，首尾插入删除性能较优。
 * 如果list的列表长度很长，使用时我们一定要关注链表相关操作的时间复杂度。


 */
public class TestRedisList {

	public static void main(String[] args) {
		// 本地先起redis服务,默认端口号为6379

		Jedis jedis = new Jedis("127.0.0.1", 6379);
		System.out.println(jedis.lrange("list", 0, -1));

		// rpush 
		//jedis.rpush("list", "java");
		//jedis.rpush("list", "python","c++");//覆盖
		
		//lpop 
		//System.out.println(jedis.lpop("list"));
		//System.out.println(jedis.lpop("list"));
		
		//llen
		System.out.println(jedis.llen("list"));//
		
		//lrange
		System.out.println(jedis.lrange("list", 0, -1));
		
		//lset 修改
		//jedis.lset("list", 0, "python_0");
		
		//从左边插入，如果第一个c++ 之前插入c#,后面的c++之前不处理；
		//jedis.linsert("list", LIST_POSITION.BEFORE, "c++", "c#");
		
		//删除元素 列表的删除操作也不是通过指定下标来确定元素的，你需要指定删除的最大个数以及元素的值
		//[python_0, c#, c#, c++, java, python, c++, java, python, c++, java, python, c++]

		//jedis.lrem("list", 1, "java");
		
		//[python_0, c#, c#, c++, python, c++, java, python, c++, java, python, c++]
		//jedis.lrem("list", 1, "c#");
		
		
		//jedis.lrem("list", 4, "c++");
		//System.out.println(jedis.lrange("list", 0, -1));
		
		System.out.println("content:"+jedis.llen("content"));

		
		//定长列表 ltrim 
		jedis.rpush("content", "java","python","c++","c#","scala","javascript");
		System.out.println(jedis.llen("content"));
		//ltrim  有效期只有一次，后续rpush 还是会改变list长度和内容
		System.out.println(jedis.ltrim("content", 0, 3));
		System.out.println(jedis.lrange("content", 0, -1));
	
		jedis.rpush("content", "java","python","c++","c#","scala","javascript");
		System.out.println(jedis.llen("content"));
		System.out.println(jedis.ltrim("content", 0, 3));
		System.out.println(jedis.lrange("content", 0, -1));
		
		//del delete element
		System.out.println(jedis.del("content"));
		
		jedis.close();
		
	}

}
