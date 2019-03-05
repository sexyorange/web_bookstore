package wdg.bookstore.cart.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import wdg.bookstore.book.domain.Book;
import wdg.bookstore.book.service.BookService;
import wdg.bookstore.cart.domain.Cart;
import wdg.bookstore.cart.domain.CartItem;
import wdg.bookstore.cart.service.CartService;

public class CartServlet extends BaseServlet{
	private CartService cartService = new CartService();
	private BookService bookService = new BookService();
	//添加条目到购物车
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.得到车
		 * 修改登录方法，在用户登录成功后，马上在session中添加一辆购物车
		2.得到条目（图书和数量）
		  通过bid得到book对象 
		 3.把条目添加到车中*/
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		int count = Integer.parseInt(request.getParameter("count"));
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		cart.add(cartItem);
		return "f:/jsps/cart/list.jsp";
	}
	//清空购物车
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	//删除某项条目
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
