package wdg.bookstore.user.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import wdg.bookstore.user.domain.User;

public class LoginFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		/*1.��session�л�ȡ�û���Ϣ
		2.�ж�session���Ƿ�����û���Ϣ�������ڣ�����
		3.���򣬱��������Ϣ��ת����login.jsp��ʾ*/
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		User user = (User)httpRequest.getSession().getAttribute("session_user");
		if(user != null){
			chain.doFilter(request, response);
		}else{
			httpRequest.setAttribute("msg", "����û�е�¼");
			httpRequest.getRequestDispatcher("/jsps/user/login.jsp")
				.forward(httpRequest, response);
		}
			}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
