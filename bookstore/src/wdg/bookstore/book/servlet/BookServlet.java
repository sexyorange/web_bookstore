package wdg.bookstore.book.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;
import wdg.bookstore.book.service.BookService;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	//查看全部图书
	public String findAll(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException,IOException{
		req.setAttribute("bookList", bookService.findAll());
		return "f:/jsps/book/list.jsp";
	}
	//按分类查找图书
	public String findByCategory(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException,IOException{
		String cid = req.getParameter("cid");
		req.setAttribute("bookList", bookService.findByCategory(cid));
		return "f:/jsps/book/list.jsp";
	}
	//加载显示具体某本图书
	public String load(HttpServletRequest req,HttpServletResponse resp)
	throws ServletException,IOException{
		String bid = req.getParameter("bid");
		req.setAttribute("book", bookService.load(bid));
		return "f:/jsps/book/desc.jsp";
	}

}
