package wdg.bookstore.book.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import wdg.bookstore.book.domain.Book;
import wdg.bookstore.book.service.BookService;
import wdg.bookstore.category.domain.Category;
import wdg.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	//查看所有图书
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	//加载某本图书第一步
	public String addPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*查询所有分类，保存到request，转发到add.jsp
		add.jsp中把所有的分类使用下拉列表显示在表单中*/
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	//加载某本图书
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	//删除图书
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		bookService.delete(bid);
		return findAll(request,response);
	}
	//修改图书
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//封装表单数据(要添加一个隐藏字段，把图片传过来)
		Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(category);
		bookService.edit(book);
		return findAll(request, response);
	}
}
