package com.hesyun.web.framework.web;

import javax.servlet.http.HttpServletRequest;

import com.lsxy.framework.core.security.SecurityUser;

public abstract class AbstractController {

	public SecurityUser getCurrentUser(HttpServletRequest request){
		SecurityUser user = (SecurityUser)request.getSession().getAttribute("currentUser");
		return user;
	}
}
