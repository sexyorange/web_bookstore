package wdg.bookstore.category.servlet.admin;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wdg.bookstore.category.domain.Category;
import wdg.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	//查询所有分类
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//添加分类
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(request,response);
	}
	//删除分类
	public String delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		try{
			categoryService.delete(cid);
			return findAll(request,response);
		}catch(CategoryException e){
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}	
	}
	//修改分类之前的加载工作
	public String editPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("category", categoryService.load(cid));
			return "f:/adminjsps/admin/category/mod.jsp";
		}	
	//修改分类
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(),Category.class);
		categoryService.edit(category);
		return findAll(request,response);
		}	
}
