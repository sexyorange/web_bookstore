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
	//��Ӷ���

	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//��session�л�ȡcart
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		//��cartת��Ϊorder����
		//1.����order���󣬲���������
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(new Date());
		order.setState(1);//���ö���״̬Ϊ1����ʾδ����
		User user = (User) request.getSession().getAttribute("session_user");
		order.setOwner(user);
		order.setTotal(cart.getTotal());
		//2.����������Ŀ���� cartItemList--->orderItemList
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		//ѭ������Cart�е�����CartItem��ʹ��ÿһ��CartItem���󴴽�OrderItem���󣬲���ӵ�������
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
		//3.����orderService��Ӷ���
		orderService.add(order);
		//4.����order��request��ת����/jsps/order/desc.jsp
		request.setAttribute("order", order);
		return "f:/jsps/order/desc.jsp";
	}
	//�ҵĶ���
	public String myOrders(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("session_user");
		List<Order> orderList = orderService.myOrders(user.getUid());
		request.setAttribute("orderList", orderList);
		return "f:/jsps/order/list.jsp";
	}
	//���ض���
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.�õ�oid����
		2.ʹ��oid����service�����õ�order
		3.���浽request��ת����/jsps/order/desc.jsp*/
		request.setAttribute("order", orderService.load(request.getParameter("oid")));
		return "f:/jsps/order/desc.jsp";
	}
	//ȷ���ջ�
	public String confirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.�õ�oid����
		2.����service����
		   �����쳣�������쳣��Ϣ��ת����msg.jsp
		3.����ɹ���Ϣ��׼����msg.jsp
		*/
		String oid = request.getParameter("oid");
		try {
			orderService.confirm(oid);
			request.setAttribute("msg", "ȷ���ջ��ɹ�");
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
		}
		
		return "f:/jsps/msg.jsp";
	}
}
