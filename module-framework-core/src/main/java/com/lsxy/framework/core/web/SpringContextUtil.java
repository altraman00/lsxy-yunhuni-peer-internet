package com.lsxy.framework.core.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SpringContextUtil
{
  private static ApplicationContext context;

  public static void setApplicationContext(ApplicationContext acx)
  {
    context = acx;
  }

  public static ApplicationContext getApplicationContext() {
    return context;
  }

  public static Object getBean(String name) throws BeansException {
    return context.getBean(name);
  }

  
public static Object getBean(String name, Class requiredType) throws BeansException
  {
    return context.getBean(name, requiredType);
  }
public static Object getBean(Class requiredType) throws BeansException
{
  return context.getBean(requiredType);
}
  public static boolean containsBean(String name) {
    return context.containsBean(name);
  }

  public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    return context.isSingleton(name);
  }

  public static Class getType(String name) throws NoSuchBeanDefinitionException {
    return context.getType(name);
  }

  public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
    return context.getAliases(name);
  }
}