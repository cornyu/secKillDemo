package com.ff.second.kill.web.dao;

import java.util.List;

import com.ff.second.kill.web.pojo.StockOrder;

/** 
* @author cornyu 
* @version 创建时间：2018年11月11日 上午11:03:27 
* 类说明 
*/
public interface IStockOrderDao {
	
	public List<StockOrder> selectAll();
	public int insertSelective(StockOrder order);
	public int deleteAllById(long sid);
	
}
