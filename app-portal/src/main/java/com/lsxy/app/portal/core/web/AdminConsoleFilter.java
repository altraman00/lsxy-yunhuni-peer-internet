package com.lsxy.app.portal.core.web;

import com.lsxy.framework.core.security.SecurityUser;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * Servlet Filter implementation class SecurityFilter
 */
public class AdminConsoleFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdminConsoleFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		SecurityUser user = (SecurityUser) req.getSession().getAttribute("currentUser");
		if(user!=null){
			if(!user.getIsAdmin() && !user.isHsyOperator()){
				//非管理员则进入用户界面
				String url ="/";
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
		}else{
			String url ="/login.jsp";
			request.getRequestDispatcher(url).forward(request, response);
		}
		chain.doFilter(request, response);
	}


	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
