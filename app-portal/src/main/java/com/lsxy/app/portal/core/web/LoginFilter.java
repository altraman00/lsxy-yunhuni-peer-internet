package com.lsxy.app.portal.core.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsxy.framework.core.utils.StringUtil;

/**
 * 2014/01/01
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
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
	public void doFilter(ServletRequest requestx, ServletResponse responsex, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) requestx;
		HttpServletResponse response = (HttpServletResponse) responsex;
		boolean checked = checkValidateCode(request);
		if(!checked){
			String url ="/login.jsp?er=vcer";
			request.getRequestDispatcher(url).forward(request, response);
		}else{
			chain.doFilter(request, response);
		}
	}

	/**
	 * 检查验证码是否正确
	 * @param request
	 * @return
	 */
	private boolean checkValidateCode(HttpServletRequest request) {
		String code = request.getParameter("validateCode");
		String currentCode = (String) request.getSession().getAttribute("SystemCode");
		
		if(currentCode != null && StringUtil.isNotEmpty(code) && code.toLowerCase().equals(currentCode.toLowerCase())){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
