package com.ff.second.kill.web.pojo;
/** 
* @author cornyu 
* @version 创建时间：2018年11月11日 上午11:05:32 
* 类说明 
* 订单类
* 
* DROP TABLE IF EXISTS `stock_order`;
CREATE TABLE `stock_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sid` bigint(20) NOT NULL  COMMENT '库存id',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '商品名称',
  `sale` int(11) NOT NULL COMMENT '已售',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index1` (`id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/

import java.sql.Timestamp;

public class StockOrder {
	
	public long id;
	public long sid;
	public String name;
	public int sale;
	public Timestamp create_time;
	public String remark;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSale() {
		return sale;
	}
	public void setSale(int sale) {
		this.sale = sale;
	}
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
