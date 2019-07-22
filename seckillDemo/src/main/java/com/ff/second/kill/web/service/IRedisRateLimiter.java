package com.ff.second.kill.web.service;

import java.io.IOException;

/** 
* @author cornyu 
* @version 创建时间：2018年11月17日 下午9:49:51 
* 类说明 
*/
public interface IRedisRateLimiter {
	public boolean isActionAllow(String userId,String actionKey,int period,int maxCount) throws IOException;
}
