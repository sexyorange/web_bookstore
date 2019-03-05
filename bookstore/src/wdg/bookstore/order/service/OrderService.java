package wdg.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import wdg.bookstore.order.dao.OrderDao;
import wdg.bookstore.order.domain.Order;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	//添加订单（需要处理事务）
	public void add(Order order){
		try{
			//开启事务
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			//提交事务
			JdbcUtils.commitTransaction();
		}catch(Exception e){
			//回滚事务
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
				throw new RuntimeException(e);
			}
		}
	}
	public List<Order> myOrders(String uid) {
		return orderDao.findByUid(uid);
	}
	//加载订单
	public Order load(String oid) {
		return orderDao.load(oid);
	}
	//确认收货
	public void confirm(String oid) throws OrderException{
		/*1.校验订单状态，若不是3，抛出异常
		 *2.修改订单状态为4，表示交易成功  
		 * */
		int state = orderDao.getStateByOid(oid);
		if(state != 3) throw new OrderException("订单确认失败，请勿非法闯入");
		
		orderDao.updateState(oid, 4);
		
	}	
}
