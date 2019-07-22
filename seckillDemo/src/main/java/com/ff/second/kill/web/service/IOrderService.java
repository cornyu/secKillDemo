package com.ff.second.kill.web.service;

import java.util.Map;

import com.ff.second.kill.web.pojo.Stock;

/** 
* @author cornyu 
* @version 创建时间：2018年11月11日 下午12:15:17 
* 类说明 
*/
public interface IOrderService {
	public void setCount(int count);
	public String createOrder(int sid);
	public Stock getStock(Map input);

	
}
