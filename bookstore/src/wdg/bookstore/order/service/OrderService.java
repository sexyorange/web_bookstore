package wdg.bookstore.order.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.jdbc.JdbcUtils;
import wdg.bookstore.order.dao.OrderDao;
import wdg.bookstore.order.domain.Order;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	//��Ӷ�������Ҫ��������
	public void add(Order order){
		try{
			//��������
			JdbcUtils.beginTransaction();
			orderDao.addOrder(order);
			orderDao.addOrderItemList(order.getOrderItemList());
			//�ύ����
			JdbcUtils.commitTransaction();
		}catch(Exception e){
			//�ع�����
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
	//���ض���
	public Order load(String oid) {
		return orderDao.load(oid);
	}
	//ȷ���ջ�
	public void confirm(String oid) throws OrderException{
		/*1.У�鶩��״̬��������3���׳��쳣
		 *2.�޸Ķ���״̬Ϊ4����ʾ���׳ɹ�  
		 * */
		int state = orderDao.getStateByOid(oid);
		if(state != 3) throw new OrderException("����ȷ��ʧ�ܣ�����Ƿ�����");
		
		orderDao.updateState(oid, 4);
		
	}	
}
