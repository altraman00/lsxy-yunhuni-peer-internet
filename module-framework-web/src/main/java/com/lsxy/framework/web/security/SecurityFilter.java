package com.lsxy.framework.web.security;

import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.core.utils.EncryptDecryptData;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet Filter implementation class SecurityFilter
 */
public abstract class SecurityFilter implements Filter {
	private Log logger = LogFactory.getLog(SecurityFilter.class);
	public String[] excludePath = null;

    /**
     * Default constructor.
     */
    public SecurityFilter() {
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
		WebUtils.logRequestParams(request);
		String requrl = request.getRequestURI();
		requrl = requrl.replace(request.getContextPath(), "");
		Object currentUser = request.getSession().getAttribute("currentUser");
		String username = request.getRemoteUser();
		if(logger.isDebugEnabled()){
			logger.debug(String.format("remote user :" + username));
		}

//		boolean isAnonymous = (currentUser == null?true:false);
		try {
			if(currentUser != null){
//				logger.debug("currentUser != null");
				SecurityUser su = (SecurityUser) currentUser;
				if(!su.getUserName().equals(username)){
					currentUser = null;
				}
			}

			if(currentUser == null){
				if(StringUtil.isNotEmpty(username)){
					SecurityUserRepository sur = getSecurityUserRepository();
					if(sur != null){
						SecurityUser su = sur.loadSecurityUser(username);
						request.getSession().setAttribute("currentUser", su);
//						isAnonymous = false;
					}else{
						logger.error("未有正确设置SecurityUserRepository");
					}


				} else {
//					isAnonymous = true;
				}
			}


//			if(isAnonymous){
//				logger.debug("the request is annonymouse access");
//				if(!isExcludePath(request,requrl)){
//					logger.debug("the request uri is not in exclude path,goto the login page");
//					String url ="/login.jsp";
//					request.getRequestDispatcher(url).forward(request, response);
//					return;
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	/**
	 * 通过抽象方法注入SecurityUserRepository的实现
	 * @return
	 *      SecurityUserRepository的具体实现
     */
	public abstract SecurityUserRepository getSecurityUserRepository();

	/**
	 * 是否是被排除的路径
	 * @param request
	 * @return
	 */
	private boolean isExcludePath(HttpServletRequest request, String requrl) {
		if(excludePath != null){
			for (String p : excludePath) {
				if(requrl.startsWith(p.trim())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		String excludePaths = fConfig.getInitParameter("excludePaths");
		if(StringUtil.isNotEmpty(excludePaths)){
			excludePath = excludePaths.split("\n");
		}
	}

//
	/**
	 * 创建和声云Token cookie
	 * @param response
	 */
	private void createHesyunToken(HttpServletRequest request,HttpServletResponse response,String	username) {
		long time = new Date().getTime();
		String token = this.getHsyToken(request);
		if(StringUtil.isEmpty(token)){
			logger.debug("not found token cookie,generat it");
			try {
				String encodeToken = EncryptDecryptData.encrypt(SystemConfig.getProperty("DES_ENCRYPT_KEY","376B4A409E5789CE"), time+"_"+username);
				logger.debug("login token:"+encodeToken);
				Cookie cookie = new Cookie("hesyuntoken", encodeToken);
				cookie.setDomain(SystemConfig.getProperty("system.sso.domain",".hesyun.com"));
				cookie.setHttpOnly(true);
				cookie.setPath("/");
				response.addCookie(cookie);
			} catch (Exception ex){
				ex.printStackTrace();
			}

		}
	}


	/**
	 * 从请求对新中获取hsytoken cookie value
	 *
	 * @param request
	 * @return
	 */
	private String getHsyToken(HttpServletRequest request) {
		String token = null;

		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("hesyuntoken")){
					token = cookie.getValue();
					break;
				}
			}
		}
		return token;
	}

}
