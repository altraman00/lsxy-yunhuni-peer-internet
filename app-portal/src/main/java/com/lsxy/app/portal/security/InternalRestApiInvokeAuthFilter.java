package com.lsxy.app.portal.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsxy.app.portal.web.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lsxy.framework.config.SystemConfig;

/**
 * 内部接口调用权限控制，只允许配置文件中指定的ip可以访问内部接口
 * @author tandy
 *
 */
public class InternalRestApiInvokeAuthFilter extends OncePerRequestFilter {

	private Log logger = LogFactory.getLog(InternalRestApiInvokeAuthFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String ip = WebUtils.getRemoteAddress(request);
		
		String ips = SystemConfig.getProperty("hscloud.restapi.allow.ips","*");
		if(!ips.equals("*")){
			if(!(ips.indexOf(ip)>=0)){
				logger.debug("调用内部接口【"+ip+"】【DIED】【"+request.getRequestURI()+"】");
				throw new AccessDeniedException("调用内部接口失败：ip被拦截: " + ip);
			}
		}
		logger.debug("调用内部接口【"+ip+"】【ALLOWED】【"+request.getRequestURI()+"】");
		filterChain.doFilter(request, response);
	}
	

}
