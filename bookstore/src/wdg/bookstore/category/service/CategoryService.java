package wdg.bookstore.category.service;

import java.util.List;

import wdg.bookstore.book.dao.BookDao;
import wdg.bookstore.category.dao.CategoryDao;
import wdg.bookstore.category.domain.Category;
import wdg.bookstore.category.servlet.admin.CategoryException;

public class CategoryService {
private CategoryDao categoryDao = new CategoryDao();
private BookDao bookDao = new BookDao();
    //��ѯ���з���
	public List<Category> findAll() {
	return categoryDao.findAll();
}
	//��ӷ���
	public void add(Category category) {
		categoryDao.add(category);
		
	}
	//ɾ������
	public void delete(String cid) throws CategoryException {
		int count = bookDao.getCountByCid(cid);
		if(count > 0) throw new CategoryException("�÷����»���ͼ�飬����ɾ����");
		categoryDao.delete(cid);
		
	}
	//����ָ������
	public Object load(String cid) {
		
		return categoryDao.load(cid);
	}
	
	public void edit(Category category) {
		categoryDao.edit(category);
		
	}
}
