package com.lsxy.app.portal.tenant.web;

import com.hesyun.web.framework.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 租户界面相关请求控制器
 * 
 * @author WangYun
 *
 */
@Controller
public class TenantController extends AbstractController {

	/**
	 * 租户管理员页面跳转
	 * @return
	 */
	@RequestMapping(value = "/tenant/admin/index", method = RequestMethod.GET)
	public String tenantAdminlogin() {
		return "tenant/admin/index";
	}


}
