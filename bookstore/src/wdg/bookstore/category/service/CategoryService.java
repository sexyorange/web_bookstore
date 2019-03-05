package wdg.bookstore.category.service;

import java.util.List;

import wdg.bookstore.book.dao.BookDao;
import wdg.bookstore.category.dao.CategoryDao;
import wdg.bookstore.category.domain.Category;
import wdg.bookstore.category.servlet.admin.CategoryException;

public class CategoryService {
private CategoryDao categoryDao = new CategoryDao();
private BookDao bookDao = new BookDao();
    //查询所有分类
	public List<Category> findAll() {
	return categoryDao.findAll();
}
	//添加分类
	public void add(Category category) {
		categoryDao.add(category);
		
	}
	//删除分类
	public void delete(String cid) throws CategoryException {
		int count = bookDao.getCountByCid(cid);
		if(count > 0) throw new CategoryException("该分类下还有图书，不能删除！");
		categoryDao.delete(cid);
		
	}
	//加载指定分类
	public Object load(String cid) {
		
		return categoryDao.load(cid);
	}
	
	public void edit(Category category) {
		categoryDao.edit(category);
		
	}
}
