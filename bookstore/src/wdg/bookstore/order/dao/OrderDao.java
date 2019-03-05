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
	//插入订单
	public void addOrder(Order order){
		try{
			String sql = "insert into orders values(?,?,?,?,?,?)";
			//要处理util的Date转换成sql的Timestamp
			Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
			Object[] params = {order.getOid(),timestamp,
					order.getTotal(),order.getState(),order.getOwner().getUid(),
					order.getAddress()}; 
			qr.update(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//插入订单条目
	public void addOrderItemList(List<OrderItem> orderItemList){
	   /* QueryRunner类的batch(String sql,Object[][] params)
	    其中params是多个一维数组（可以把一维数组当成是一个塑料袋，二维数组就是装塑料袋的塑料袋）
	    每个一维数组都与sql在一起执行一次，多个一维数组就执行多次*/
		try{
			String sql = "insert into orderitem values(?,?,?,?,?)";
			/*把orderItemList转换成二维数组
			把一个OrderItem对象转换成一个一维数组*/
			//左边的是二维数组中一维数组的个数，右边的是每个一维数组中能装的元素
			Object[][] params = new Object[orderItemList.size()][];
			//循环遍历orderItemList，使用每个orderItem对象为params中每一个一维数组赋值
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
	//按uid查询订单(查询多个订单)
	public List<Order> findByUid(String uid) {
		try{
			//1.通过uid查询出当前用户的所有List<Order>
			String sql = "select * from orders where uid=?";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
			//2.循环遍历每个order，为其加载他所有的订单条目
			for(Order order : orderList){
				loadOrderItems(order);//为order对象添加它的所有订单条目
			}
			return orderList;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	private void loadOrderItems(Order order) throws SQLException {
		//查询orderitem、book两张表
		String sql = "select * from book b,orderitem i where i.bid=b.bid and oid=?";
		//因一行结果集对应的不再是一个Javabean，所以不能再使用BeanListHandler，而是MapListHandler
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(),order.getOid());
		//我们需要使用一个Map生成两个对象：OrderItem、Book，然后再建立两者的关系（把Book设置给Order）
		//循环遍历每个Map,使用map生成两个对象，然后建立关系（最终结果是一个OrderItem），把OrderItem保存起来
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}
	//把mapList中每个Map转换成两个对象，并建立关系
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map:mapList){
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}
	//把一个map转换为一个OrderItem对象
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	//通过oid查询单个订单
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
	//通过oid查询订单状态
	public int getStateByOid(String oid){
		
		try {
			String sql = "select state from orders where oid =?";
			return (Integer)qr.query(sql, new ScalarHandler(), oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	//更改订单状态
	public void updateState(String oid, int state){
		
		try {
			String sql = "update orders set state=?  where oid =?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	} 
}
