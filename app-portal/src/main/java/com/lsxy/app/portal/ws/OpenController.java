package com.lsxy.app.portal.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 开放调用的动作，没有登录都可以访问的动作
 * @author tandy
 *
 */
@Controller
@RequestMapping(value="/open")
public class OpenController {
	

	private static Log logger = LogFactory.getLog(OpenController.class);

	
}
