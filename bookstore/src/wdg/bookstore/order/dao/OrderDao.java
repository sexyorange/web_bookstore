package wdg.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import wdg.bookstore.book.domain.Book;
import wdg.bookstore.order.domain.Order;
import wdg.bookstore.order.domain.OrderItem;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	//���붩��
	public void addOrder(Order order){
		try{
			String sql = "insert into orders values(?,?,?,?,?,?)";
			//Ҫ����util��Dateת����sql��Timestamp
			Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
			Object[] params = {order.getOid(),timestamp,
					order.getTotal(),order.getState(),order.getOwner().getUid(),
					order.getAddress()}; 
			qr.update(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//���붩����Ŀ
	public void addOrderItemList(List<OrderItem> orderItemList){
	   /* QueryRunner���batch(String sql,Object[][] params)
	    ����params�Ƕ��һά���飨���԰�һά���鵱����һ�����ϴ�����ά�������װ���ϴ������ϴ���
	    ÿ��һά���鶼��sql��һ��ִ��һ�Σ����һά�����ִ�ж��*/
		try{
			String sql = "insert into orderitem values(?,?,?,?,?)";
			/*��orderItemListת���ɶ�ά����
			��һ��OrderItem����ת����һ��һά����*/
			//��ߵ��Ƕ�ά������һά����ĸ������ұߵ���ÿ��һά��������װ��Ԫ��
			Object[][] params = new Object[orderItemList.size()][];
			//ѭ������orderItemList��ʹ��ÿ��orderItem����Ϊparams��ÿһ��һά���鸳ֵ
			for(int i = 0;i<orderItemList.size();i++){
				OrderItem item = orderItemList.get(i);
				params[i]=new Object[]{item.getIid(),item.getCount(),
						item.getSubtotal(),item.getOrder().getOid(),
						item.getBook().getBid()};
			}
			qr.batch(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//��uid��ѯ����(��ѯ�������)
	public List<Order> findByUid(String uid) {
		try{
			//1.ͨ��uid��ѯ����ǰ�û�������List<Order>
			String sql = "select * from orders where uid=?";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
			//2.ѭ������ÿ��order��Ϊ����������еĶ�����Ŀ
			for(Order order : orderList){
				loadOrderItems(order);//Ϊorder��������������ж�����Ŀ
			}
			return orderList;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	private void loadOrderItems(Order order) throws SQLException {
		//��ѯorderitem��book���ű�
		String sql = "select * from book b,orderitem i where i.bid=b.bid and oid=?";
		//��һ�н������Ӧ�Ĳ�����һ��Javabean�����Բ�����ʹ��BeanListHandler������MapListHandler
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(),order.getOid());
		//������Ҫʹ��һ��Map������������OrderItem��Book��Ȼ���ٽ������ߵĹ�ϵ����Book���ø�Order��
		//ѭ������ÿ��Map,ʹ��map������������Ȼ������ϵ�����ս����һ��OrderItem������OrderItem��������
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}
	//��mapList��ÿ��Mapת�����������󣬲�������ϵ
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map:mapList){
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}
	//��һ��mapת��Ϊһ��OrderItem����
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	//ͨ��oid��ѯ��������
	public Order load(String oid) {
		try{
			
			String sql = "select * from orders where oid=?";
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
			loadOrderItems(order);
			
			return order;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	//ͨ��oid��ѯ����״̬
	public int getStateByOid(String oid){
		
		try {
			String sql = "select state from orders where oid =?";
			return (Integer)qr.query(sql, new ScalarHandler(), oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	//���Ķ���״̬
	public void updateState(String oid, int state){
		
		try {
			String sql = "update orders set state=?  where oid =?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	} 
}
