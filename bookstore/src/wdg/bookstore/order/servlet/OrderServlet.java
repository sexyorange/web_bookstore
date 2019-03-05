package wdg.bookstore.order.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wdg.bookstore.cart.domain.Cart;
import wdg.bookstore.cart.domain.CartItem;
import wdg.bookstore.order.domain.Order;
import wdg.bookstore.order.domain.OrderItem;
import wdg.bookstore.order.service.OrderException;
import wdg.bookstore.order.service.OrderService;
import wdg.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	//添加订单

	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//从session中获取cart
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		//吧cart转换为order对象
		//1.创建order对象，并设置属性
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);//设置订单状态为1，表示未付款
		User user = (User) request.getSession().getAttribute("session_user");
		order.setOwner(user);
		order.setTotal(cart.getTotal());
		//2.创建订单条目集合 cartItemList--->orderItemList
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//循环遍历Cart中的所有CartItem，使用每一个CartItem对象创建OrderItem对象，并添加到集合中
		for(CartItem cartItem : cart.getCartItems()){
			OrderItem oi = new OrderItem();
			oi.setBook(cartItem.getBook());
			oi.setIid(CommonUtils.uuid());
			oi.setCount(cartItem.getCount());
			oi.setSubtotal(cartItem.getSubtotal());
			oi.setOrder(order);
			
			orderItemList.add(oi);
		}
		order.setOrderItemList(orderItemList);
		cart.clear();
		//3.调用orderService添加订单
		orderService.add(order);
		//4.保存order到request域，转发到/jsps/order/desc.jsp
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	//我的订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOrders(user.getUid());
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}
	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.得到oid参数
		2.使用oid调用service方法得到order
		3.保存到request域，转发到/jsps/order/desc.jsp*/
		request.setAttribute("order", orderService.load(request.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}
	//确认收货
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.得到oid参数
		2.调用service方法
		   若有异常，保存异常信息，转发到msg.jsp
		3.保存成功信息，准发到msg.jsp
		*/
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "确认收货成功");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		
		return "f:/jsps/msg.jsp";
	}
}
