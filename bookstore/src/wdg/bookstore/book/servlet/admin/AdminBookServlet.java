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
	//�鿴����ͼ��
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("bookList", bookService.findAll());
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/list.jsp";
	}
	//����ĳ��ͼ���һ��
	public String addPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*��ѯ���з��࣬���浽request��ת����add.jsp
		add.jsp�а����еķ���ʹ�������б���ʾ�ڱ���*/
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/add.jsp";
	}
	//����ĳ��ͼ��
	public String load(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		Book book = bookService.load(bid);
		request.setAttribute("book", book);
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	
	//ɾ��ͼ��
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bid = request.getParameter("bid");
		bookService.delete(bid);
		return findAll(request,response);
	}
	//�޸�ͼ��
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//��װ������(Ҫ���һ�������ֶΣ���ͼƬ������)
		Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(category);
		bookService.edit(book);
		return findAll(request, response);
	}
}
