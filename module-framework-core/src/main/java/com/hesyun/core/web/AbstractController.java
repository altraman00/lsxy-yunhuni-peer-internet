package com.hesyun.core.web;

import javax.servlet.http.HttpServletRequest;

import com.hesyun.core.security.SecurityUser;

public abstract class AbstractController {

	public SecurityUser getCurrentUser(HttpServletRequest request){
		SecurityUser user = (SecurityUser)request.getSession().getAttribute("currentUser");
		return user;
	}
}
