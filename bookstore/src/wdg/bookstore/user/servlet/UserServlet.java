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
	
	//注册功能
	public String regist(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		//封装表单数据到form对象
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		//补全uid、code
		form.setUid(CommonUtils.uuid());
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		//创建一个map，用来封装错误信息
		Map<String,String> errors = new HashMap<String,String>();
		String username = form.getUsername();
		if(username == null || username.trim().isEmpty()){
			errors.put("username", "用户名不能为空");
		}else if(username.length() < 3 || username.length() > 10){
			errors.put("username", "用户名长度应在3~10之间");
		}
		String password = form.getPassword();
		if(password == null || password.trim().isEmpty()){
			errors.put("password", "密码不能为空");
		}else if(password.length() < 3 || password.length() > 10){
			errors.put("password", "密码长度应在3~10之间");
		}
		String email = form.getEmail();
		if(email == null || email.trim().isEmpty()){
			errors.put("email", "用户名不能为空");
		}else if(!email.matches("\\w+@\\w+\\.\\w+")){
			errors.put("email", "email格式错误");
		}
		if(errors.size() > 0){
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		
		try{
			userService.regist(form);
		}catch(UserException e){
			//抛出异常
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/regist.jsp";
		}
		/*未抛出异常
		1.发邮件
		      准备配置文件
		2.保存成功信息
		3.准发到msg.jsp*/
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
		content = MessageFormat.format(content, form.getCode());//替换{0}占位符
		Session session = MailUtils.createSession(host, uname, pwd);//得到session
		Mail mail = new Mail(from,to,subject,content);//创建邮件
		try {
			MailUtils.send(session, mail);//发邮件
		} catch (MessagingException e) {
			
		}
		
		request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱激活");
		return "f:/jsps/msg.jsp";
		
		
		
	};
	public String active(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		String code = request.getParameter("code");
		try {
			userService.active(code);
			request.setAttribute("msg", "恭喜您，您已激活成功");
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
			//给用户中添加一辆购物车，既向session中保存一cart对象
			request.getSession().setAttribute("cart", new Cart());
			return "r:/index.jsp";
			
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("form", form);
			return "f:/jsps/user/login.jsp";
		}
		
	}
	//退出功能-把session销毁
	public String quit(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException ,IOException {
		request.getSession().invalidate();
		return "r:/login.jsp";
	}
}
