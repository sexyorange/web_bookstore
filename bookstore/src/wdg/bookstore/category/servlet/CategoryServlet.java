package wdg.bookstore.category.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

import wdg.bookstore.category.service.CategoryService;

public class CategoryServlet extends BaseServlet{
	private CategoryService categoryService = new CategoryService();
	public String findAll(HttpServletRequest req,HttpServletResponse resp)
		throws ServletException,IOException{
		req.setAttribute("categoryList", categoryService.findAll());
		return "f:/jsps/left.jsp";
	}

}
