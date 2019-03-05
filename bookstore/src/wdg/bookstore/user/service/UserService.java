package wdg.bookstore.user.service;

import wdg.bookstore.user.dao.UserDao;
import wdg.bookstore.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();
	public void regist(User form) throws UserException{
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("该用户名已注册");
		
		user = userDao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("该邮箱已被注册");
		 
		userDao.add(form);
		
	}
	public void active(String code) throws UserException{
		User user = userDao.findByCode(code);
		if(user == null) throw new UserException("激活码无效");
		if(user.isState()) throw new UserException("您已经激活过了");
		userDao.updateState(user.getUid(), true);//修改用户的状态
	}
	//登录功能
	public User login(User form) throws UserException{
		User user = userDao.findByUsername(form.getUsername());
		if(user == null) throw new UserException("用户名未注册");
		if(!user.getPassword().equals(form.getPassword())) throw new UserException("密码错误"); 
//		if(!form.isState()) throw new UserException("您还未激活");
		return user;
	}
}
