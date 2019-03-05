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
	//�����Ŀ�����ﳵ
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*1.�õ���
		 * �޸ĵ�¼���������û���¼�ɹ���������session�����һ�����ﳵ
		2.�õ���Ŀ��ͼ���������
		  ͨ��bid�õ�book���� 
		 3.����Ŀ��ӵ�����*/
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
	//��չ��ﳵ
	public String clear(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		cart.clear();
		return "f:/jsps/cart/list.jsp";
	}
	//ɾ��ĳ����Ŀ
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		String bid = request.getParameter("bid");
		cart.delete(bid);
		return "f:/jsps/cart/list.jsp";
	}
}
