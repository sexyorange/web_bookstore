package wdg.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import wdg.bookstore.book.domain.Book;
import wdg.bookstore.category.domain.Category;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	//查询所有图书
	public List<Book> findAll(){
		try{
			String sql = "select * from book where del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//按分类查询
	public List<Book> findByCategory(String cid){
		try{
			String sql = "select * from book where cid = ? and del=false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class),cid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	public Book findByBid(String bid) {
		try{
			//我们需要在Book对象中保存Category的信息
			String sql = "select * from book where bid = ?";
			//使用一个Map,映射出两个对象，再给这两个对象建立关系
			Map<String,Object> map = qr.query(sql, new MapHandler(),bid);
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//查询指定分类下的图书本数
	public int getCountByCid(String cid) {
		try{
			String sql = "select count(*) from book where cid = ? and del=false";
			Number cnt = (Number) qr.query(sql, new ScalarHandler(),cid);
			return cnt.intValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	//添加图书
	public void add(Book book) {
		try{
			String sql = "insert into book values(?,?,?,?,?,?)";
			Object[] params={book.getBid(),book.getBname(),book.getPrice(),
					book.getAuthor(),book.getImage(),book.getCategory().getCid()};
			qr.update(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	//删除图书其实并不是真的删除，而是不查询它而已。
	public void delete(String bid) {
		try{
			String sql = "update book set del=true where bid=?";
			qr.update(sql, bid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	public void edit(Book book) {
		try{
			String sql = "update book set bname=?,price=?,author=?,image=?,cid=? where bid=?";
			Object[] params={book.getBname(),book.getPrice(),book.getAuthor(),
					book.getImage(),book.getCategory().getCid(),book.getBid()};
			qr.update(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
}
