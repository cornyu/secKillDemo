package com.ff.second.kill.web.dao;


import java.util.List;
import java.util.Map;

import com.ff.second.kill.web.pojo.Stock;

/** 
* @author cornyu 
* @version 创建时间：2018年11月11日 上午11:03:10 
* 类说明 
*/
public interface IStockDao {
	public List<Stock> selectAll();
	public Stock getStockById(long id);
	public int updateStockById(Stock stock);
	public int updateCount(Stock stock);
	
	
	public int updateByOptimistic(Stock stock);
	
	public Stock getStock(Map arg);
}
