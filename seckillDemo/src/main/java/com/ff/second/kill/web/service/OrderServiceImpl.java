package com.ff.second.kill.web.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ff.second.kill.web.dao.IStockDao;
import com.ff.second.kill.web.dao.IStockOrderDao;
import com.ff.second.kill.web.pojo.Stock;
import com.ff.second.kill.web.pojo.StockOrder;

/**
 * @author cornyu
 * @version 创建时间：2018年11月11日 下午12:14:54 类说明
 */
@Service
public class OrderServiceImpl implements IOrderService {

	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Resource
	private IStockDao stockDao;
	
	@Resource
	private IStockOrderDao stockOrderDao;

	@Override
	public void setCount(int count) {
		logger.info("跟新库存："+count);
		
		// 更新库存  //1 表示产品id
		Stock stock = stockDao.getStockById(1);
		stock.setCount(Integer.valueOf(count));
		stock.setSale(0);//销售个数 初始化为0
		stock.setVersion(0);//版本号 初始化为0
		stockDao.updateCount(stock);
		
		//删除已有订单数据
		stockOrderDao.deleteAllById(1);
		
	}

	/**
	 * 扣减 库存
	 */
    @Transactional
	@Override
	public String createOrder(int sid) {		
    		// 校验库存
		Stock stock = checkStock(sid);

		// 扣库存
		//saleStock(stock);
		
		//使用乐观锁来实现库存扣减
		reduceStockByOptimistic(stock);
		
		// 创建订单
		int id = createOrder(stock);
		
		return String.valueOf(id);

	}
    
    /**
	 * 检查是否还有库存
	 * 
	 * @param sid  产品id
	 * @return
	 */
	private Stock checkStock(int sid) {
		Stock stock = stockDao.getStockById(sid);
		if (stock.getSale() == stock.getCount()) {
			logger.info("库存不足，已经卖了："+stock.getSale() + ";总共："+stock.getCount());
			throw new RuntimeException("库存不足");
		}
		return stock;
	}
	
	/**
	 * stock表中扣减库存,增加sale字段的值
	 * 
	 * @param stock
	 * @return
	 */
	private int saleStock(Stock stock) {
		stock.setSale(stock.getSale() + 1);
		return stockDao.updateStockById(stock);
	}
	

	/**
	 * 订单表stock_order中创建订单
	 * 
	 * @param stock
	 * @return
	 */
	private int createOrder(Stock stock) {
		StockOrder order = new StockOrder();
		order.setSid(stock.getId());
		order.setName(stock.getName());
		order.setSale(stock.getSale());
		int id = stockOrderDao.insertSelective(order);
		return id;
	}
	
	
	private void reduceStockByOptimistic(Stock stock) {
		stock.setSale(stock.getSale() + 1);
		int count = stockDao.updateByOptimistic(stock);	
		if (count == 0){
			logger.error("并发更新库存失败了！");
	        throw new RuntimeException("并发更新库存失败") ;
	    }
	}
	
	public Stock getStock(Map input) {
		return stockDao.getStock(input);
	}


}
