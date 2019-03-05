package wdg.bookstore.user.service;

import wdg.bookstore.user.dao.UserDao;
import wdg.bookstore.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();
	public void regist(User form) throws UserException{
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) throw new UserException("���û�����ע��");
		
		user = userDao.findByEmail(form.getEmail());
		if(user != null) throw new UserException("�������ѱ�ע��");
		 
		userDao.add(form);
		
	}
	public void active(String code) throws UserException{
		User user = userDao.findByCode(code);
		if(user == null) throw new UserException("��������Ч");
		if(user.isState()) throw new UserException("���Ѿ��������");
		userDao.updateState(user.getUid(), true);//�޸��û���״̬
	}
	//��¼����
	public User login(User form) throws UserException{
		User user = userDao.findByUsername(form.getUsername());
		if(user == null) throw new UserException("�û���δע��");
		if(!user.getPassword().equals(form.getPassword())) throw new UserException("�������"); 
//		if(!form.isState()) throw new UserException("����δ����");
		return user;
	}
}
