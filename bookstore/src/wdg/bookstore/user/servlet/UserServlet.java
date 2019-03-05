package wdg.bookstore.user.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import wdg.bookstore.cart.domain.Cart;
import wdg.bookstore.user.domain.User;
import wdg.bookstore.user.service.UserException;
import wdg.bookstore.user.service.UserService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(1111);
		super.doPost(req, resp);
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	//ע�Ṧ��
	public String regist(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		//��װ�����ݵ�form����
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		//��ȫuid��code
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		//����һ��map��������װ������Ϣ
		Map<String,String> errors = new HashMap<String,String>();
		String username = form.getUsername();
		if(username == null || username.trim().isEmpty()){
			errors.put("username", "�û�������Ϊ��");
		}else if(username.length() < 3 || username.length() > 10){
			errors.put("username", "�û�������Ӧ��3~10֮��");
		}
		String password = form.getPassword();
		if(password == null || password.trim().isEmpty()){
			errors.put("password", "���벻��Ϊ��");
		}else if(password.length() < 3 || password.length() > 10){
			errors.put("password", "���볤��Ӧ��3~10֮��");
		}
		String email = form.getEmail();
		if(email == null || email.trim().isEmpty()){
			errors.put("email", "�û�������Ϊ��");
		}else if(!email.matches("\\w+@\\w+\\.\\w+")){
			errors.put("email", "email��ʽ����");
		}
		if(errors.size() > 0){
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		try{
			userService.regist(form);
		}catch(UserException e){
			//�׳��쳣
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		/*δ�׳��쳣
		1.���ʼ�
		      ׼�������ļ�
		2.����ɹ���Ϣ
		3.׼����msg.jsp*/
		Properties props = new Properties();
		props.load(this.getClass().getClassLoader()
				.getResourceAsStream("email_template.properties"));
		String host = props.getProperty("host");
		String uname = props.getProperty("uname");
		String pwd = props.getProperty("pwd");
		String from = props.getProperty("from");
		String to = form.getEmail();
		String subject = props.getProperty("subject");
		String content = props.getProperty("content");
		content = MessageFormat.format(content, form.getCode());//�滻{0}ռλ��
		Session session = MailUtils.createSession(host, uname, pwd);//�õ�session
		Mail mail = new Mail(from,to,subject,content);//�����ʼ�
		try {
			MailUtils.send(session, mail);//���ʼ�
		} catch (MessagingException e) {
			
		}
		
		request.setAttribute("msg", "��ϲ��ע��ɹ��������ϵ����伤��");
		return "f:/jsps/msg.jsp";
		
		
		
	};
	public String active(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		String code = request.getParameter("code");
		try {
			userService.active(code);
			request.setAttribute("msg", "��ϲ�������Ѽ���ɹ�");
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
		
	}
	public String login(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		try {
			User user = userService.login(form);
			request.getSession().setAttribute("session_user", user);
			//���û������һ�����ﳵ������session�б���һcart����
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
			
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
	}
	//�˳�����-��session����
	public String quit(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		request.getSession().invalidate();
		return "r:/login.jsp";
	}
}
