package com.lsxy.app.portal.security;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lsxy.framework.core.security.SecurityUser;

/**
 * 客服中心连接过滤
 * @author WangYun
 *
 */
public class TUserFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication token = SecurityContextHolder.getContext().getAuthentication();
		String url = null;
		if(token == null || token instanceof AnonymousAuthenticationToken || !token.isAuthenticated()) {
			logger.debug("have no login  auth token ,redirect to login page");
			url ="/login.jsp";
			request.getRequestDispatcher(url).forward(request, response);
			return;
		} else {
			// 获取当前请求的renque uri
			url = request.getRequestURL().toString();
			String urlPrefix = request.getServerName() + "/" + request.getContextPath();
			String requestPath = url.substring(urlPrefix.length());
			// 获取当前登录用户权限
			SecurityUser su = (SecurityUser)request.getSession().getAttribute("currentUser");
			if (su != null) {
				logger.debug("found curent User :"+su.getLoginName());
				Map<String, Boolean> authMap = su.getAuthMap();
				Set<String> authKeys = authMap.keySet();
				Iterator<String> authKeysIt = authKeys.iterator();
				
				// 判断是否有请求的权限
				Boolean hasAuth = false;
				while(authKeysIt.hasNext()) {
					String authKey = authKeysIt.next();
					
					if (requestPath.indexOf(authKey) != -1) {
						hasAuth = true;
						break;
					}
				}
				
				if (hasAuth) {
					logger.debug("has auth:"+request.getRequestURL());
					filterChain.doFilter(request, response);
				} else {
					throw new AccessDeniedException("权限不足");
				}
			} else {
				url ="/login.jsp";
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
		}
	}

}
