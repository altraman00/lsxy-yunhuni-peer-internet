package com.lsxy.framework.web.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.lsxy.framework.config.SystemConfig;

/**
 * 注销后的处理Handler
 * 1.移除hesyuntoken
 * @author tandy
 *
 */
public class OnLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	public OnLogoutSuccessHandler(String defaultTargetUrl){
		this.setDefaultTargetUrl(defaultTargetUrl);
	}
	private static final Log logger = LogFactory.getLog(OnLogoutSuccessHandler.class);
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		logger.debug("user logout successful,removed hesyuntoken");
		removeHesyunTokenCookie(response);
		super.onLogoutSuccess(request, response, authentication);
	}
	/**
	 * 移除和声云token
	 * @param response
	 */
	private void removeHesyunTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("hesyuntoken", null);
		cookie.setDomain(SystemConfig.getProperty("system.sso.domain",".hesyun.com"));
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
