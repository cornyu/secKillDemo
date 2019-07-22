package com.ff.second.kill.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ff.second.kill.web.dao.IStockDao;
import com.ff.second.kill.web.pojo.Stock;
import com.ff.second.kill.web.service.IOrderService;
import com.ff.second.kill.web.service.IRedisRateLimiter;

/** 
* @author cornyu 
* @version 创建时间：2018年11月11日 上午11:44:07 
* 类说明 
*/

@Controller
public class OrderController {

	private static Logger logger = LoggerFactory.getLogger(OrderController.class);  


	@Resource
	private IOrderService orderService;
	
	@Resource
	private IRedisRateLimiter limiter;
	

	@RequestMapping("/getStock")
	public void getStock(HttpSession session,
			HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
		String id = httpServletRequest.getParameter("id");
		logger.info("get库存："+id);
		Map input = new HashMap();
		input.put("id", httpServletRequest.getParameter("id"));
		input.put("name", httpServletRequest.getParameter("name"));
		input.put("sale", httpServletRequest.getParameter("sale"));
		input.put("remark", httpServletRequest.getParameter("remark"));
		logger.info("input："+input);

		//更新库存 
		Stock stock = orderService.getStock(input);
		
		Map<String, Object> result = new HashMap<String ,Object>();
		result.put("status", "success");
		result.put("stock", stock);
		logger.info("stock："+stock);

		printResult(result,response);
		
	}
	
	@RequestMapping("/setCount")
	public void setCount(HttpSession session,
			HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
		String count = httpServletRequest.getParameter("count");
		logger.info("初始化库存："+count);
		
		//更新库存 
		orderService.setCount(Integer.valueOf(count));
		
		Map<String, Object> result = new HashMap<String ,Object>();
		result.put("status", "success");
		
		printResult(result,response);
		
	}
	
	@RequestMapping("/reduce")
	public void createOrder(HttpSession session,
			HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String ,Object>();
		result.put("status", "success");

		//开始限流
		boolean limitResult = limiter.isActionAllow("user", "createOrder", 1, 50);
		//boolean limitResult = true;
		if(!limitResult) {
			logger.info("请求被限流了！");
			result.put("status", "failure");
		}else {		
			try {
				logger.info("扣件库存开始");
				//默认 扣减id = 1的商品
				String orderId = orderService.createOrder(1);				
			}
			catch(Exception e) {
				result.put("status", "failure");
			}			
		}					
	    printResult(result,response);			
	}

	
	private void printResult(Map<String, Object> result,HttpServletResponse response) throws IOException {
		String jsonString = JSON.toJSONString(result);
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsonString);
	}
	
	
}
