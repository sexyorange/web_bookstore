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
	//��ѯ���з���
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("categoryList", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//��ӷ���
	public String add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(request,response);
	}
	//ɾ������
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
	//�޸ķ���֮ǰ�ļ��ع���
	public String editPre(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		request.setAttribute("category", categoryService.load(cid));
			return "f:/adminjsps/admin/category/mod.jsp";
		}	
	//�޸ķ���
	public String edit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(request.getParameterMap(),Category.class);
		categoryService.edit(category);
		return findAll(request,response);
		}	
}
