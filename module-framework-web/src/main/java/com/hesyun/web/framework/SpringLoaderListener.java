package com.hesyun.web.framework;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lsxy.framework.core.web.SpringContextUtil;

public class SpringLoaderListener extends ContextLoaderListener
{
  public void contextInitialized(ServletContextEvent event)
  {
    super.contextInitialized(event);
    ServletContext context = event.getServletContext();
    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    SpringContextUtil.setApplicationContext(ctx);
  }
}  